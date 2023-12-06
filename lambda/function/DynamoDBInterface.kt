package lambda.function

interface DynamoDBInterface {
    // 1
    suspend fun putOrUpdateItem(keyName: String, keyVal: String, tableNameVal: String, latitude: String, 
                        longitude: String, altitude: String, date: String, time: String): String
}