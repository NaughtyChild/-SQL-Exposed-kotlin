import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

const val O_URL = "jdbc:mysql://localhost:3306/testDB?characterEncoding=UTF-8&useSSL=false&verifyServerCertificate=no"
const val O_DIVER_CLASS = "com.mysql.jdbc.Driver"
//创建表，id和name
object Branches : IntIdTable() {
    val name = varchar("name", length = 30)
}
//表实体
class Branch(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Branch>(Branches)
    var name by Branches.name
}

fun main() {
    Database.connect(O_URL, user = "root", password = "1234", driver = O_DIVER_CLASS)
    transaction {
        create(Branches)
        Branch.new {
            name = "信息部"
        }
        val branch = Branch.new { name = "技术部" }
        o_showDatas()
        branch.name = "科技部"
        o_showDatas()
    }
}

fun o_showDatas() {
    Branches.selectAll().forEach {
        println("showDatas:${it.get(Branches.id)}:${it[Branches.name]}")
    }
}