package com.decode.mybooksummaries.presentation

import androidx.annotation.DrawableRes
import com.decode.mybooksummaries.R

sealed class OnboardingModel(
    @DrawableRes val image: Int,
    val title: String,
    val description: String
) {
    data object FirstPage: OnboardingModel(
        image = R.drawable.img1,
        title = "Kitap Dünyanıza Hoş Geldiniz!",
        description = "Kitaplarınızı zahmetsizce yönetin, özetler ve alıntılar ekleyin ve okuma deneyiminizi bir üst seviyeye taşıyın."
    )
    data object SecondPage: OnboardingModel(
        image = R.drawable.img2,
        title = "Kitaplarınızı Kolayca Yönetin",
        description = "Kitap kütüphanenizi oluşturun, kitaplarınızı kategorilere ayırın ve okunma durumlarını takip edin. Artık kitaplarınızı kaybetmeyin!"
    )
    data object ThirdPage: OnboardingModel(
        image = R.drawable.img3,
        title = "Özetler ve Alıntılar",
        description = "Okuduğunuz kitapların özetlerini kaydedin ve en sevdiğiniz alıntıları saklayın. Bilgilere istediğiniz zaman kolayca erişin!"
    )
    data object FourthPage: OnboardingModel(
        image = R.drawable.img5,
        title = "Hadi Başlayalım!",
        description = "Kitap kütüphanenizi oluşturmaya hazır mısınız? 'Başla' butonuna tıklayın ve okuma deneyiminizi dönüştürün!"
    )
}