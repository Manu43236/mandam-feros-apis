package com.api.feros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/schema")
public class SchemaAnalyzerController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/all-tables")
    public Map<String, Object> getAllTablesSchema() {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> allTables = new ArrayList<>();
        
        try (Connection connection = dataSource.getConnection()) {
            String databaseName = connection.getCatalog();
            DatabaseMetaData metaData = connection.getMetaData();
            
            // Get all tables
            ResultSet tables = metaData.getTables(databaseName, null, "%", new String[]{"TABLE"});
            
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                Map<String, Object> tableInfo = new HashMap<>();
                tableInfo.put("tableName", tableName);
                
                // Get columns for this table
                List<Map<String, String>> columns = new ArrayList<>();
                ResultSet columnsRs = metaData.getColumns(databaseName, null, tableName, null);
                
                while (columnsRs.next()) {
                    Map<String, String> columnInfo = new HashMap<>();
                    columnInfo.put("columnName", columnsRs.getString("COLUMN_NAME"));
                    columnInfo.put("dataType", columnsRs.getString("TYPE_NAME"));
                    columnInfo.put("size", columnsRs.getString("COLUMN_SIZE"));
                    columnInfo.put("nullable", columnsRs.getString("IS_NULLABLE"));
                    columnInfo.put("defaultValue", columnsRs.getString("COLUMN_DEF"));
                    columns.add(columnInfo);
                }
                
                tableInfo.put("columns", columns);
                
                // Get primary keys
                List<String> primaryKeys = new ArrayList<>();
                ResultSet pkRs = metaData.getPrimaryKeys(databaseName, null, tableName);
                while (pkRs.next()) {
                    primaryKeys.add(pkRs.getString("COLUMN_NAME"));
                }
                tableInfo.put("primaryKeys", primaryKeys);
                
                // Get foreign keys
                List<Map<String, String>> foreignKeys = new ArrayList<>();
                ResultSet fkRs = metaData.getImportedKeys(databaseName, null, tableName);
                while (fkRs.next()) {
                    Map<String, String> fkInfo = new HashMap<>();
                    fkInfo.put("column", fkRs.getString("FKCOLUMN_NAME"));
                    fkInfo.put("referencesTable", fkRs.getString("PKTABLE_NAME"));
                    fkInfo.put("referencesColumn", fkRs.getString("PKCOLUMN_NAME"));
                    foreignKeys.add(fkInfo);
                }
                tableInfo.put("foreignKeys", foreignKeys);
                
                allTables.add(tableInfo);
            }
            
            result.put("database", databaseName);
            result.put("totalTables", allTables.size());
            result.put("tables", allTables);
            result.put("success", true);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
            e.printStackTrace();
        }
        
        return result;
    }
}
