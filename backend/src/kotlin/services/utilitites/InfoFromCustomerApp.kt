import services.dtos.ClientToken

class InfoFromCustomerApp {
    fun getToken() :ClientToken{
        val token = message.token
        InfoToRemoteService.sendToken()
        return ClientToken(token)
    }
}