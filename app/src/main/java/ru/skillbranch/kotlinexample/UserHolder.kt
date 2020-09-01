package ru.skillbranch.kotlinexample

import androidx.annotation.VisibleForTesting

object UserHolder {
    private val map = mutableMapOf<String, User>()

    fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): User {
        return User.makeUser(fullName = fullName, email = email, password = password)
            .also { user ->
                if (!map.contains(user.login)) map[user.login] = user
                else throw IllegalArgumentException("A user with this email already exists")
            }
    }

    fun loginUser(login: String, password: String): String? {
        var normalizedLogin = login
        if (normalizedLogin.startsWith("+")) normalizedLogin =
            normalizedLogin.replace("[^+\\d]".toRegex(), "")
        return map[normalizedLogin.trim()]?.run {
            if (checkPassword(password)) this.userInfo
            else null
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun clearHolder() {
        map.clear()
    }

    fun registerUserByPhone(fullName: String, phone: String): User {
        return User.makeUser(fullName = fullName, phone = phone).also { user ->
            if (!map.containsKey(user.login)) map[user.login] = user
            else throw IllegalArgumentException("A user with this phone already exists")
        }
    }

    fun requestAccessCode(login: String): Unit {
        var normalizedLogin = login
        if (normalizedLogin.startsWith("+")) normalizedLogin =
            normalizedLogin.replace("[^+\\d]".toRegex(), "")
        map[normalizedLogin.trim()].run {
            this?.changeAccessCode()
            this?.userInfo
        }
    }
}