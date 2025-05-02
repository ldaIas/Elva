package net.elva.core

/**
 * The Purpose encapsulates the reason for a program existing.
 * It abstracts the concepts of an internal state and the inevitable loop that occurs in applications.
 * The initialization of the model can be thought of as the "main" method.
 * The update method is the "main" loop that is "controlled" by the runtime.
 * The surface is the touch point between outside interactions. 
 * This is what the outside world "sees" and how it can interact with the internal program.
 */
interface Purpose<in R: ElvaRecord, out E: ElvaRecord> {
    /**
     * This is the "main" equivalent. Program args sent to model producer
     */
    fun initModel(vararg args: String): E
    fun update(inModel: R, inMsg: Msg): Pair<E, List<Effect>>
    fun surface(): Surface<R, Msg>
}