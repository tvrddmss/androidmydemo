package esa.mydemo.ui.imageview.card

import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import esa.mylibrary.utils.MyImageUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async

class CreditCardsViewModel : ViewModel() {

    private val stream = MutableLiveData<CreditCardsModel>()

    val modelStream: LiveData<CreditCardsModel>
        get() = stream

    private var imageUrls = mutableListOf<String>(
        "https://ts1.cn.mm.bing.net/th/id/R-C.b99bc05a21929e0c9505858f72cb1ec5?rik=n73B272KiPYAkg&riu=http%3a%2f%2fimg.name2012.com%2fuploads%2fallimg%2f2014-10%2f10-025302_225.jpg&ehk=%2fdgK3P0MtdPeQAKLyQ6eZvu5rAjxXScWI47b2y14Mqk%3d&risl=&pid=ImgRaw&r=0",
        "https://img0.baidu.com/it/u=1702308803,1451099157&fm=253&fmt=auto?w=130&h=170",
        "https://img2.baidu.com/it/u=3057131346,3793235073&fm=253&fmt=auto?w=130&h=170",
        "https://img1.baidu.com/it/u=2149676754,3217378425&fm=253&fmt=auto?w=130&h=170",
        "https://img0.baidu.com/it/u=3147111282,3015253826&fm=253&fmt=auto?w=130&h=170",
        "https://img2.baidu.com/it/u=2849596744,2672677820&fm=253&fmt=auto?w=130&h=170",
        "https://img1.baidu.com/it/u=982591807,3234407955&fm=253&fmt=auto?w=130&h=170",
        "https://lmg.jj20.com/up/allimg/tx08/0816201965147.jpg",
        "http://img.duoziwang.com/2018/01/2023085831866.jpg",
        "https://img0.baidu.com/it/u=1631675928,1435603040&fm=253&fmt=auto?w=130&h=170",
        "https://img0.baidu.com/it/u=410342904,312326097&fm=253&fmt=auto?w=130&h=170",
        "https://img2.baidu.com/it/u=3563523272,3635825002&fm=253&fmt=auto?w=130&h=170",
        "https://img2.baidu.com/it/u=4248747809,221650016&fm=253&fmt=auto?w=130&h=170"
    )

    private var imageBitmap = mutableListOf<Bitmap?>()

    private var data: List<CreditCardModel> = kotlin.collections.listOf(
        CreditCardModel(
            backgroundColor = Color.parseColor("#e57373"),
            bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888)
        ),
    )
    //= listOf(
//        CreditCardModel(backgroundColor = Color.parseColor("#e57373"), imageUrl = imageUrls.get(0)),
//        CreditCardModel(backgroundColor = Color.parseColor("#ef9a9a"), imageUrl = imageUrls.get(1)),
//        CreditCardModel(backgroundColor = Color.parseColor("#ffcdd2"), imageUrl = imageUrls.get(2)),
//        CreditCardModel(backgroundColor = Color.parseColor("#ffebee"), imageUrl = imageUrls.get(3)),
//        CreditCardModel(backgroundColor = Color.parseColor("#ef5350"), imageUrl = imageUrls.get(4)),
//        CreditCardModel(backgroundColor = Color.parseColor("#f44336"), imageUrl = imageUrls.get(5)),
//        CreditCardModel(backgroundColor = Color.parseColor("#e53935"), imageUrl = imageUrls.get(6)),
//        CreditCardModel(backgroundColor = Color.parseColor("#d32f2f"), imageUrl = imageUrls.get(7)),
//        CreditCardModel(backgroundColor = Color.parseColor("#c62828"), imageUrl = imageUrls.get(8)),
//        CreditCardModel(backgroundColor = Color.parseColor("#b71c1c"), imageUrl = imageUrls.get(9)),
//        CreditCardModel(backgroundColor = Color.parseColor("#880e4f")),
//        CreditCardModel(backgroundColor = Color.parseColor("#ad1457")),
//        CreditCardModel(backgroundColor = Color.parseColor("#c2185b")),
//        CreditCardModel(backgroundColor = Color.parseColor("#d81b60")),
//        CreditCardModel(backgroundColor = Color.parseColor("#e91e63")),
//        CreditCardModel(backgroundColor = Color.parseColor("#ec407a")),
//        CreditCardModel(backgroundColor = Color.parseColor("#f06292")),
//        CreditCardModel(backgroundColor = Color.parseColor("#f48fb1")),
//        CreditCardModel(backgroundColor = Color.parseColor("#f8bbd0")),
//        CreditCardModel(backgroundColor = Color.parseColor("#fce4ec")),
//
//        CreditCardModel(backgroundColor = Color.parseColor("#fff8e1")),
//        CreditCardModel(backgroundColor = Color.parseColor("#ffecb3")),
//        CreditCardModel(backgroundColor = Color.parseColor("#ffe082")),
//        CreditCardModel(backgroundColor = Color.parseColor("#ffd54f")),
//        CreditCardModel(backgroundColor = Color.parseColor("#ffca28")),
//        CreditCardModel(backgroundColor = Color.parseColor("#ffc107")),
//        CreditCardModel(backgroundColor = Color.parseColor("#ffb300")),
//        CreditCardModel(backgroundColor = Color.parseColor("#ffa000")),
//        CreditCardModel(backgroundColor = Color.parseColor("#ff8f00")),
//        CreditCardModel(backgroundColor = Color.parseColor("#ff6f00")),
//
//        CreditCardModel(backgroundColor = Color.parseColor("#bf360c")),
//        CreditCardModel(backgroundColor = Color.parseColor("#d84315")),
//        CreditCardModel(backgroundColor = Color.parseColor("#e64a19")),
//        CreditCardModel(backgroundColor = Color.parseColor("#f4511e")),
//        CreditCardModel(backgroundColor = Color.parseColor("#ff5722")),
//        CreditCardModel(backgroundColor = Color.parseColor("#ff7043")),
//        CreditCardModel(backgroundColor = Color.parseColor("#ff8a65")),
//        CreditCardModel(backgroundColor = Color.parseColor("#ffab91")),
//        CreditCardModel(backgroundColor = Color.parseColor("#ffccbc")),
//        CreditCardModel(backgroundColor = Color.parseColor("#fbe9e7"))
//    )

    private var currentIndex = 0

    val swipeLeftEnable: MutableLiveData<Boolean> = MutableLiveData(true)
    val swipeRightEnable: MutableLiveData<Boolean> = MutableLiveData(true)


    private val cardFourLeft: CreditCardModel?
        get() {
            if (currentIndex in 4..data.size - 1) {
                return data[(currentIndex - 4)]
            } else {
                return null
            }
        }
    private val cardThreeLeft: CreditCardModel?
        get() {
            if (currentIndex in 3..data.size - 1) {
                return data[(currentIndex - 3)]
            } else {
                return null
            }
        }
    private val cardTwoLeft: CreditCardModel?
        get() {
            if (currentIndex in 2..data.size - 1) {
                return data[(currentIndex - 2)]
            } else {
                return null
            }
        }
    private val cardOneLeft: CreditCardModel?
        get() {
            if (currentIndex in 1..data.size - 1) {
                return data[(currentIndex - 1)]
            } else {
                return null
            }
        }
    private val cardCenter: CreditCardModel?
        get() {
            if (currentIndex in 0..data.size - 1) {
                return data[currentIndex]
            } else {
                return null
            }
        }
    private val cardOneRight: CreditCardModel?
        get() {
            if (currentIndex in 0..data.size - 2) {
                return data[(currentIndex + 1)]
            } else {
                return null
            }
        }
    private val cardTwoRight: CreditCardModel?
        get() {
            if (currentIndex in 0..data.size - 3) {
                return data[(currentIndex + 2)]
            } else {
                return null
            }
        }
    private val cardThreeRight: CreditCardModel?
        get() {
            if (currentIndex in 0..data.size - 4) {
                return data[(currentIndex + 3)]
            } else {
                return null
            }
        }
    private val cardFourRight: CreditCardModel?
        get() {
            if (currentIndex in 0..data.size - 5) {
                return data[(currentIndex + 4)]
            } else {
                return null
            }
        }

    private val scope = MainScope()

    fun updateData() {
        data = kotlin.collections.listOf(
            esa.mydemo.ui.imageview.card.CreditCardModel(
                backgroundColor = android.graphics.Color.parseColor("#e57373"),
                bitmap = imageBitmap.get(0)!!
            ),
            esa.mydemo.ui.imageview.card.CreditCardModel(
                backgroundColor = android.graphics.Color.parseColor("#ef9a9a"),
                bitmap = imageBitmap.get(1)!!
            ),
            esa.mydemo.ui.imageview.card.CreditCardModel(
                backgroundColor = android.graphics.Color.parseColor("#ffcdd2"),
                bitmap = imageBitmap.get(2)!!
            ),
            esa.mydemo.ui.imageview.card.CreditCardModel(
                backgroundColor = android.graphics.Color.parseColor("#ffebee"),
                bitmap = imageBitmap.get(3)!!
            ),
            esa.mydemo.ui.imageview.card.CreditCardModel(
                backgroundColor = android.graphics.Color.parseColor("#ef5350"),
                bitmap = imageBitmap.get(4)!!
            ),
            esa.mydemo.ui.imageview.card.CreditCardModel(
                backgroundColor = android.graphics.Color.parseColor("#f44336"),
                bitmap = imageBitmap.get(5)!!
            ),
            esa.mydemo.ui.imageview.card.CreditCardModel(
                backgroundColor = android.graphics.Color.parseColor("#e53935"),
                bitmap = imageBitmap.get(6)!!
            ),
            esa.mydemo.ui.imageview.card.CreditCardModel(
                backgroundColor = android.graphics.Color.parseColor("#d32f2f"),
                bitmap = imageBitmap.get(7)!!
            ),
            esa.mydemo.ui.imageview.card.CreditCardModel(
                backgroundColor = android.graphics.Color.parseColor("#c62828"),
                bitmap = imageBitmap.get(8)!!
            ),
            esa.mydemo.ui.imageview.card.CreditCardModel(
                backgroundColor = android.graphics.Color.parseColor("#b71c1c"),
                bitmap = imageBitmap.get(9)!!
            ),
        )

        updateCards()
    }

    fun getImage() {
        CoroutineScope(Dispatchers.IO).async {
            for (url in imageUrls) {
                var bitmap = MyImageUtil.getBitmap(url)
                if (bitmap == null) {
                    imageBitmap.add(
                        null
                    )
                } else {
                    imageBitmap.add(bitmap)
                }
            }
            updateData()
        }
    }

    init {
        getImage()
        updateCards()
    }

    fun swipeRight() {
        currentIndex += 1
        currentIndex %= data.size
//        swipeLeftEnable.postValue(currentIndex < data.size - 1)
        updateCards()
    }

    fun swipeLeft() {
        if (currentIndex == 0) {
            currentIndex = data.size - 1
        } else {
            currentIndex -= 1
        }
//        swipeRightEnable.postValue(currentIndex > 0)
        updateCards()
    }

    private fun updateCards() {
        stream.postValue(
            CreditCardsModel(
                cardOneLeft = cardOneLeft,
                cardTwoLeft = cardTwoLeft,
                cardThreeLeft = cardThreeLeft,
                cardFourLeft = cardFourLeft,
                cardCenter = cardCenter,
                cardOneRight = cardOneRight,
                cardTwoRight = cardTwoRight,
                cardThreeRight = cardThreeRight,
                cardFourRight = cardFourRight
            )
        )


    }

}