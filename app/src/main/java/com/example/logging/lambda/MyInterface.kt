package com.example.logging.lambda

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction

interface MyInterface {
    @LambdaFunction
    fun writeDynamoDB(request: RequestClass?): ResponseClass?
}