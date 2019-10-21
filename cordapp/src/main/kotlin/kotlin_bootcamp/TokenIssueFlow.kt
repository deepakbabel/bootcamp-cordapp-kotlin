package kotlin_bootcamp

import co.paralleluniverse.fibers.Suspendable
import net.corda.core.flows.*
import net.corda.core.identity.Party
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.ProgressTracker

/* Our flow, automating the process of updating the ledger.
 */
@InitiatingFlow
@StartableByRPC
class TokenIssueFlow(private val owner: Party, private val amount: Int) : FlowLogic<SignedTransaction>() {

    override val progressTracker: ProgressTracker = ProgressTracker()

    @Suspendable
    @Throws(FlowException::class)
    override fun call(): SignedTransaction {
        // We choose our transaction's notary (the notary prevents double-spends).
        val notary = serviceHub.networkMapCache.notaryIdentities[0]
        // We get a reference to our own identity.
        val issuer = ourIdentity

        /* ============================================================================
         *         TODO 1 - Create our TokenState to represent on-ledger tokens!
         * ===========================================================================*/
        // We create our new TokenState.
        //val tokenState: TokenState? = null
        val tokenState = TokenState(ourIdentity,owner, amount)

        /* ============================================================================
         *      TODO 3 - Build our token issuance transaction to update the ledger!
         * ===========================================================================*/
        // We build our transaction.
        //val transactionBuilder: TransactionBuilder? = null
        val transactionBuilder: TransactionBuilder = TransactionBuilder()
        transactionBuilder.notary = notary
        //transactionBuilder.addInputState(null) as no input state is present
        transactionBuilder.addOutputState(tokenState, TokenContract.ID)
        transactionBuilder.addCommand(TokenContract.Commands.Issue(), ourIdentity.owningKey)
        //transactionBuilder.addCommand(TokenContract.Commands.Issue(), ourIdentity.owningKey, owner.owningKey)

        /* ============================================================================
         *          TODO 2 - Write our TokenContract to control token issuance!
         * ===========================================================================*/
        // We check our transaction is valid based on its contracts.
        //transactionBuilder!!.verify(serviceHub)
        transactionBuilder.verify(serviceHub)

        // We sign the transaction with our private key, making it immutable.
        val signedTransaction = serviceHub.signInitialTransaction(transactionBuilder)

        // We get the transaction notarised and recorded automatically by the platform.
        return subFlow(FinalityFlow(signedTransaction))
    }
}

@InitiatedBy(TokenIssueFlow::class)
class TokenIssueFlowResponder(flowSession: FlowSession) : FlowLogic<Void>(){
    @Suspendable
    override fun call(): Void {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}