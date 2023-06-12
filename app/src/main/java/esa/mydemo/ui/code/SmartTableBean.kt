package esa.mydemo.ui.code

//@SmartTable(name = "销售计划表")
class SmartTableBean(groupId: String, codeValue: String, codeText: String) {


    //    name：版块名称，count：目标值，restaurant：餐饮数量，ka：KA数量，wholesale：流通批发数量，industry：工业加工数量，other：其它数量
//    @SmartColumn(id = 0, name = "组ID", autoMerge = true)
    private var groupId: String = ""

//    @SmartColumn(id = 1, name = "codeValue")
    private var codeValue: String = ""

//    @SmartColumn(id = 2, name = "codeText")
    private var codeText: String = ""

    init {
        this.groupId = groupId
        this.codeValue = codeValue
        this.codeText = codeText
    }
}