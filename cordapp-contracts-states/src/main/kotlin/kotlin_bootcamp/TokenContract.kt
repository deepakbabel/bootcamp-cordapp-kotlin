package kotlin_bootcamp

import net.corda.core.contracts.*
import net.corda.core.transactions.LedgerTransaction
import java.lang.IllegalArgumentException

/* Our contract, governing how our state will evolve over time.
 */
class TokenContract : Contract  {
    companion object {
        const val ID = "kotlin_bootcamp.TokenContract"
    }

    //db: see https://docs.corda.net/api/kotlin/corda/net.corda.core.contracts/-command-data.html for reference
    interface Commands : CommandData {
        //class Redeem: TypeOnlyCommandData(), Commands
        class Issue : TypeOnlyCommandData(), Commands
    }

    override fun verify(tx: LedgerTransaction) {
        requireThat {
            //check shape
            "number of inputs is zero" using (tx.inputStates.isEmpty())
            "number of outputs is one" using (tx.outputStates.size == 1)
            "number of commands is one" using (tx.commands.size == 1)
        }

        var command = tx.commands.requireSingleCommand<Commands>()
        println(command.toString())
        when(command.value)
        {
            is Commands.Issue ->
            {
                requireThat {
                    //check contents
                    var outputState = tx.outputStates[0]
                    "output state must be a tokenstate " using (outputState is TokenState)
                    var tokenState = outputState as TokenState
                    "Amount must be positive" using (tokenState.amount > 0)
                    "command must be an Issue command" using (command.value is TokenContract.Commands.Issue)
                    //check signers
                    "Issuer must sign the transaction" using (command.signers.contains(tokenState.issuer.owningKey))
                }
            }
            else ->
                throw IllegalArgumentException("Not an issue or redeem command")
//            is Commands.Redeem ->
//            {
//                requireThat {
//                    var outputState = tx.outputStates[0]
//                    "output state must be a tokenstate " using (outputState is TokenState)
//                    var tokenState = outputState as TokenState
//                    "amount must be positive" using (tokenState.amount > 0)
//                    //check signers
//                    "Issuer must sign the transaction" using (command.signers.contains(tokenState.issuer.owningKey))
//                }
//            }
        }
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}