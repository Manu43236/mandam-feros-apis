#!/bin/bash
# FEROS API - cURL commands
# Base URL (change if needed)
BASE_URL="http://localhost:8080"

echo "=== Database Info ==="
curl -s "$BASE_URL/api/database/info" | python3 -m json.tool

echo -e "\n=== Vehicle Types - List All ==="
curl -s "$BASE_URL/api/vehicle-types"

echo -e "\n=== Vehicle Types - List Active Only ==="
curl -s "$BASE_URL/api/vehicle-types?activeOnly=true"

echo -e "\n=== Vehicle Types - Create ==="
curl -s -X POST "$BASE_URL/api/vehicle-types" \
  -H "Content-Type: application/json" \
  -d '{"name":"Truck","description":"Heavy goods vehicle","displayOrder":1}'

echo -e "\n=== Vehicle Types - Get by ID (replace {id} with actual id) ==="
# curl -s "$BASE_URL/api/vehicle-types/{id}"

echo -e "\n=== Vehicle Types - Update (replace {id} with actual id) ==="
# curl -s -X PUT "$BASE_URL/api/vehicle-types/{id}" \
#   -H "Content-Type: application/json" \
#   -d '{"name":"Truck","description":"Heavy goods vehicle","isActive":true,"displayOrder":1}'

echo -e "\n=== Vehicle Makes - List All ==="
curl -s "$BASE_URL/api/vehicle-makes"

echo -e "\n=== Vehicle Makes - Create ==="
curl -s -X POST "$BASE_URL/api/vehicle-makes" \
  -H "Content-Type: application/json" \
  -d '{"name":"Tata Motors","country":"India","displayOrder":1}'

echo -e "\n=== Vehicle Models - List All ==="
curl -s "$BASE_URL/api/vehicle-models"

echo -e "\n=== Vehicle Models - List by Make (replace {makeId} with actual id) ==="
# curl -s "$BASE_URL/api/vehicle-models?makeId={makeId}"

echo -e "\n=== Vehicle Models - List by Vehicle Type (replace {vehicleTypeId} with actual id) ==="
# curl -s "$BASE_URL/api/vehicle-models?vehicleTypeId={vehicleTypeId}"

echo -e "\n=== Vehicle Models - Create (replace makeId and vehicleTypeId with actual ids) ==="
# curl -s -X POST "$BASE_URL/api/vehicle-models" \
#   -H "Content-Type: application/json" \
#   -d '{"name":"407","make":{"id":"<make-id>"},"vehicleType":{"id":"<vehicle-type-id>"},"typicalCapacityTons":9.5,"typicalTyreCount":12,"displayOrder":0}'

echo -e "\n=== Schema - All Tables ==="
curl -s "$BASE_URL/api/schema/all-tables" | python3 -m json.tool 2>/dev/null | head -50
