package app

import tornadofx.App
import tornadofx.launch
import view.MyView

class MyApp: App(MyView::class, Styles::class)

fun main(args: Array<String>) {
    launch<MyApp>()
}