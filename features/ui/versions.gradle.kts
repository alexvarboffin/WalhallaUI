//compatVersion = "1.6.1"
//materialVersion = "1.9.0"

import java.text.SimpleDateFormat
import java.util.Date

fun versionCodeDate(): Int {
    return SimpleDateFormat("yyMMdd").format(Date()).toInt()
}