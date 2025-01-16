package presentation

import model.User

object Contract {

    sealed interface State {
        data object NoAuthorized : State
        data class Authorized(val user: User) : State
    }

    data class Message(val message: String)
}