package lambda.function

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import kotlinx.coroutines.runBlocking

class App : RequestHandler<HandlerInput, HandlerOutput> {
    
    val myInterface : DynamoDBInterface = DynamoDB()
    
    override fun handleRequest(input: HandlerInput?, context: Context?): HandlerOutput {
        input?.let {
            
            return HandlerOutput(runBlocking { 
                myInterface.putOrUpdateItem(it.keyName, it.keyVal, it.tableNameVal, it.latitude, 
                                    it.longitude, it.altitude, it.date, it.time) 
                })
        }
        return HandlerOutput("Failed");
    }
}    
