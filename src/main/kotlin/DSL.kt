import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create

const val URL = "jdbc:mysql://localhost:3306/testDB?characterEncoding=UTF-8&useSSL=false&verifyServerCertificate=no"
const val DIVER_CLASS = "com.mysql.jdbc.Driver"

object Departments : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", length = 40)
}

object Employee : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar(name = "name", length = 60)
    val dept_id = (integer("dept_id") references Departments.id).nullable()
}

fun main() {
    Database.connect(URL, driver = DIVER_CLASS, user = "root", password = "1234")
    transaction {
        logger.addLogger(StdOutSqlLogger)
        create(Departments)
        create(Employee)
        val id1 = Departments.insert {
            it[name] = "销售部"
        } get Departments.id
        val id2 = Departments.insert {
            it[name] = "技术部"
        } get Departments.id
        Employee.insert {
            it[name] = "张三"
            it[dept_id] = id1
        }
        Employee.insert {
            it[name] = "约翰纳什"
            it[dept_id] = id2
        }
        Employee.insert {
            it[name] = "王五"
            it[dept_id] = id2
        }
//        Departments.nam

        (Employee innerJoin Departments).slice(
            Employee.id,
            Employee.name,
            Departments.name,
            Departments.id,
            Employee.dept_id
        ).select {
            Departments.id eq Employee.dept_id
        }.forEach {
            println("main:${it[Employee.id]},${it[Employee.name]},${it[Departments.name]},${it[Departments.id]},${it[Employee.dept_id]}")
        }
//        showDatas()
//        Departments.deleteWhere { Departments.id lessEq 1 }
//        showDatas()
    }
}

fun showDatas() {
    Departments.selectAll().forEach {
        println("${it[Departments.id]}:${it[Departments.name]}")
    }
    Employee.selectAll().forEach {
        println("showDatas:${it[Employee.id]},${it[Employee.name]},${it[Employee.dept_id]}")
    }
}