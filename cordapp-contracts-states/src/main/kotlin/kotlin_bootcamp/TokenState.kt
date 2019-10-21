package kotlin_bootcamp

import net.corda.core.contracts.*
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party
import java.util.*

/* Our state, defining a shared fact on the ledger.
 */
class TokenState (
    val issuer: Party,
    val owner: Party,
    val amount: Int): ContractState
    //db: val address: String) : ContractState
{
    override val participants: List<AbstractParty>
        get() = listOf(issuer, owner) //To change initializer of created properties use File | Settings | File Templates.
}

class HouseState(
        val owner: Party,
        val address: String,
        val price: Int,
        val type: String
) : LinearState {
    override val linearId: UniqueIdentifier
        get() = UniqueIdentifier("PRESTIGE_VILLA")  //To change initializer of created properties use File | Settings | File Templates.
    override val participants: List<AbstractParty>
        get() = listOf(owner) //To change initializer of created properties use File | Settings | File Templates.
}

class IOUState (
        val from: Party,
        val to: Party,
        var amount: Int,
        var due: Date,
        var paid: Int
) : OwnableState
{
    override val owner: AbstractParty
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val participants: List<AbstractParty>
        get() = TODO("not implemented")//To change initializer of created properties use File | Settings | File Templates.
    override fun withNewOwner(newOwner: AbstractParty): CommandAndState {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}