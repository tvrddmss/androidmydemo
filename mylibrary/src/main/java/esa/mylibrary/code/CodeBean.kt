package esa.mylibrary.code

class CodeBean {
    var id: Int? = null
    var groupId: String = ""
    var codeValue: String = ""
    var codeText: String = ""

    class Builder {
        private val bean = CodeBean()


        private var id: Int? = null
        private var groupId: String = ""
        private var codeValue: String = ""
        private var codeText: String = ""


        fun setId(id: Int?): Builder {
            this.id = id
            return this
        }

        fun setGroupId(groupId: String): Builder {
            this.groupId = groupId
            return this
        }

        fun setCodeValue(codeValue: String): Builder {
            this.codeValue = codeValue
            return this
        }

        fun setCodeText(codeText: String): Builder {
            this.codeText = codeText
            return this
        }

        fun build(): CodeBean {
            bean.id = id
            bean.groupId = groupId
            bean.codeValue = codeValue
            bean.codeText = codeText

            return bean
        }
    }

    override fun toString(): String {
        return "StudentBean(id=$id, groupId=$groupId, codeValue=$codeValue, codeText=$codeText)"
    }

}