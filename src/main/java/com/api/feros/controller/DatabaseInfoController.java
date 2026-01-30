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
@RequestMapping("/api/database")
public class DatabaseInfoController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/info")
    public Map<String, Object> getDatabaseInfo() {
        Map<String, Object> result = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            // Get database name
            String databaseName = connection.getCatalog();
            result.put("database", databaseName);
            result.put("url", metaData.getURL());
            result.put("username", metaData.getUserName());
            result.put("databaseProductName", metaData.getDatabaseProductName());
            result.put("databaseProductVersion", metaData.getDatabaseProductVersion());
            
            // Get all tables
            List<Map<String, String>> tables = new ArrayList<>();
            ResultSet rs = metaData.getTables(databaseName, null, "%", new String[]{"TABLE"});
            
            int count = 0;
            while (rs.next()) {
                count++;
                Map<String, String> tableInfo = new HashMap<>();
                tableInfo.put("number", String.valueOf(count));
                tableInfo.put("tableName", rs.getString("TABLE_NAME"));
                tableInfo.put("tableType", rs.getString("TABLE_TYPE"));
                tables.add(tableInfo);
            }
            
            result.put("totalTables", count);
            result.put("tables", tables);
            result.put("success", true);
            result.put("message", "Database information retrieved successfully");
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Error: " + e.getMessage());
            result.put("error", e.getClass().getSimpleName());
        }
        
        return result;
    }
    
    @GetMapping("/tables")
    public Map<String, Object> getTableNames() {
        Map<String, Object> result = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection()) {
            String databaseName = connection.getCatalog();
            DatabaseMetaData metaData = connection.getMetaData();
            
            List<String> tableNames = new ArrayList<>();
            ResultSet rs = metaData.getTables(databaseName, null, "%", new String[]{"TABLE"});
            
            while (rs.next()) {
                tableNames.add(rs.getString("TABLE_NAME"));
            }
            
            result.put("database", databaseName);
            result.put("totalTables", tableNames.size());
            result.put("tables", tableNames);
            result.put("success", true);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Error: " + e.getMessage());
        }
        
        return result;
    }
}
