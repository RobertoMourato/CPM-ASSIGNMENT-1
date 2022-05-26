import com.google.gson.Gson


class InfoToRemoteService {

        @PostMapping
        fun sendToken(token: String){
                return ClientToken(token)
        }
}