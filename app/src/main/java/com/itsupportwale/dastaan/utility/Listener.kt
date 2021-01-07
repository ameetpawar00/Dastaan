package com.itsupportwale.dastaan.utility



interface ListenerIns {
    fun onReturnValue(input: ArrayList<String>?)
    fun onReturnValue(input: StringBuilder)
    fun onReturnValue(id: Int?)
}
interface ListenerSpec {
    fun onReturnValue(input: ArrayList<String>?)
    fun onReturnValue(input: StringBuilder)
    fun onReturnValue(id: Int?)

}
interface ListenerBranch {
    fun onReturnValue(input: ArrayList<String>?)
    fun onReturnValue(input: StringBuilder)
    fun onReturnValue(id: Int?)

}
interface ListenerSort {
    fun onReturnValue(input: ArrayList<String>?)
    fun onReturnValue(input: StringBuilder)
    fun onReturnValue(checkedRadioButtonId: Int)
    fun onReturnValue(sortBy: String?)
}
interface ListenerCountry {
    fun onReturnValue(input: String)
}
interface ListenerPayment {
    fun onReturnValue(input: String)
}