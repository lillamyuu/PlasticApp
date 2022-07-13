package com.example.plastic

object ClassNames {
    private val classnames = arrayListOf("01-PET", "02-PE-HD", "03-PVC", "04-PE-LD", "05-PP", "06-PS", "07-O", "08-NP")
    private val classnamesbystr = arrayListOf("pet", "pehd", "pvc", "ldpe", "pp", "ps", "o", "app")
    private val codeinfo = arrayListOf("полиэтилентерефталат: бутылки, в которых продают воду, газировку, молоко, масло",
        "полеэтилен высокой плотности, безопасен для пищи", "опасен для пищевого использования", "полиэтилен высокого давления (низкой плотности): пакеты и плёнка",
    "полипропилен: крышки для бутылок, вёдра и ведёрки, стаканчики для йогурта, упаковка линз, шуршащая пластиковая упаковка",
     "Из полистирола делают одноразовую посуду, прозрачные контейнеры. В еду, которая контактирует с полистиролом, может попадать стирол – опасный канцероген.",
    "Другие виды пластика")
    fun getClass(id: Int):String{
        return classnames[id-1]
    }
    fun getImage(key: Int):String{
        return classnamesbystr[key-1]
    }
    fun getInfo(key: Int): String{
        return codeinfo[key-1]
    }
}