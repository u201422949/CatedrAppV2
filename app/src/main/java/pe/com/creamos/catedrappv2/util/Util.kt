package pe.com.creamos.catedrappv2.util

fun validateMail(mail: String): Boolean {
    return mail.matches(regex = Regex("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$"))
}