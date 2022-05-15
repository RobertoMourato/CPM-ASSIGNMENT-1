import com.google.gson.Gson
import services.entities.Customer
import services.entities.Payment
import services.entities.Item

class InfoFromRemoteService {

    @GetMapping("/receipt")
    fun get(): PaymentInfo{
        val customer = Gson().fromJson(customer, Customer::class.java)
        val payment = Gson().fromJson(payment, Payment::class.java)
        val itemsList: MutableList<Item> = listOf()
        for(item in payment.items){
            itemsList.add(item)
        }
        return PaymentInfo(customer, itemsList, payment)
    }

}