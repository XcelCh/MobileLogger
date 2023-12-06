package lambda.function

import aws.sdk.kotlin.services.dynamodb.DynamoDbClient
import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import aws.sdk.kotlin.services.dynamodb.model.PutItemRequest
import aws.sdk.kotlin.services.dynamodb.model.UpdateItemRequest
import aws.sdk.kotlin.services.dynamodb.model.ConditionalCheckFailedException

class DynamoDB : DynamoDBInterface {
    
    override suspend fun putOrUpdateItem(keyName: String, keyVal: String, tableNameVal: String, latitude: String,
                                 longitude: String, altitude: String, date: String, time: String): String {

        val itemValues = mutableMapOf<String, AttributeValue>()
        val updateValues = mutableMapOf<String, AttributeValue>()
        val placeholder = mapOf("#loc" to "Location")
        val keyValPair = mapOf("PhoneId" to AttributeValue.S(keyVal))
        val ddb = DynamoDbClient {
            region = "us-east-1"
        }

        // Add all content to the table.
        itemValues[keyName] = AttributeValue.S(keyVal)
        itemValues["Location"] = AttributeValue.L(listOf(AttributeValue.M(
            mutableMapOf("Latitude" to AttributeValue.S(latitude), "Longitude" to AttributeValue.S(longitude),
                "Altitude" to AttributeValue.S(altitude), "Date" to AttributeValue.S(date), "Time" to AttributeValue.S(time)))))

        updateValues[":val"] = AttributeValue.L(listOf(AttributeValue.M(
            mutableMapOf("Latitude" to AttributeValue.S(latitude), "Longitude" to AttributeValue.S(longitude),
                "Altitude" to AttributeValue.S(altitude), "Date" to AttributeValue.S(date), "Time" to AttributeValue.S(time)))))

        val requestCreate = PutItemRequest {
            tableName = tableNameVal
            item = itemValues
        }

        val requestUpdate = UpdateItemRequest {
            tableName = tableNameVal
            key = keyValPair
            expressionAttributeValues = updateValues.toMap()
            expressionAttributeNames = placeholder
            conditionExpression = "attribute_exists(PhoneId)"
            updateExpression = "SET #loc = list_append(#loc, :val)"
        }

        try {
            ddb.updateItem(requestUpdate)
        } catch (e: ConditionalCheckFailedException){
            ddb.putItem(requestCreate)
        }

        return "Success"
    }
}