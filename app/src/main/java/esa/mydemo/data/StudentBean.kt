package esa.mydemo.data

class StudentBean {
    var id: Int? = null
    var date: String? = null
    var timestamp: Long = 0
    var name: String? = null

    class Builder {
        private val bean = StudentBean()


        private var id: Int? = null
        private var date: String? = null
        private var timestamp: Long = 0
        private var name: String? = null


        fun setId(id: Int?): Builder {
            this.id = id
            return this
        }

        fun setName(name: String?): Builder {
            this.name = name
            return this
        }

        fun setDate(date: String?): Builder {
            this.date = date
            return this
        }

        fun setTimeStamp(timestamp: Long): Builder {
            this.timestamp = timestamp
            return this
        }

        fun build(): StudentBean {
            bean.id = id
            bean.name = name
            bean.date = date
            bean.timestamp = timestamp

            return bean
        }
    }

    override fun toString(): String {
        return "StudentBean(id=$id, date=$date, timestamp=$timestamp, name=$name)"
    }

}