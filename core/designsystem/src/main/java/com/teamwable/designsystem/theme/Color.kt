package com.teamwable.designsystem.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

// Primary
val Purple50 = Color(0xFF8F4BFF)
val Purple100 = Color(0xFF3D059A)
val Purple10 = Color(0xFFF4EDFF)

// Secondary
val Sky50 = Color(0xFF05E8D1)
val Sky10 = Color(0xFFE4FAF8)

// System
val Success = Color(0xFF0DBE61)
val Info = Color(0xFF127CF4)
val Warning = Color(0xFFEE9209)
val Error = Color(0xFFF01F1F)
val SystemNavigationBar = Color(0xFFF5F5F5)

// Gray Scale
val Black = Color(0xFF0E0E0E)
val Gray900 = Color(0xFF2D2D2D)
val Gray800 = Color(0xFF4A4A4A)
val Gray700 = Color(0xFF6B6B6B)
val Gray600 = Color(0xFF8D8D8D)
val Gray500 = Color(0xFFAEAEAE)
val Gray400 = Color(0xFFCCCCCC)
val Gray300 = Color(0xFFDEDEDE)
val Gray200 = Color(0xFFEDEDED)
val Gray100 = Color(0xFFF7F7F7)
val White = Color(0xFFFDFDFD)

// Team Colors
val T50 = Color(0xFFE2012D)
val T10 = Color(0xFFF9E3E7)
val Gen50 = Color(0xFFAA8B30)
val Gen10 = Color(0xFFF4F1E8)
val Bro50 = Color(0xFF01492B)
val Bro10 = Color(0xFFFAEBEB)
val Drx50 = Color(0xFF1004A4)
val Drx10 = Color(0xFFE4E3F3)
val Dk50 = Color(0xFF4A4A4A)
val Dk10 = Color(0xFFEDEDED)
val Kt50 = Color(0xFFFF0A09)
val Kt10 = Color(0xFFFCE4E4)
val Fox50 = Color(0xFFD31F28)
val Fox10 = Color(0xFFFBF8DB)
val Ns50 = Color(0xFFEC1C24)
val Ns10 = Color(0xFFE3E3E3)
val Kdf50 = Color(0xFFE63312)
val Kdf10 = Color(0xFFFAE8E5)
val Hle50 = Color(0xFFF47320)
val Hle10 = Color(0xFFFBEEE6)

@Stable
class WableColors(
    purple50: Color,
    purple100: Color,
    purple10: Color,
    sky50: Color,
    sky10: Color,
    success: Color,
    info: Color,
    warning: Color,
    error: Color,
    black: Color,
    gray900: Color,
    gray800: Color,
    gray700: Color,
    gray600: Color,
    gray500: Color,
    gray400: Color,
    gray300: Color,
    gray200: Color,
    gray100: Color,
    white: Color,
    t50: Color,
    t10: Color,
    gen50: Color,
    gen10: Color,
    bro50: Color,
    bro10: Color,
    drx50: Color,
    drx10: Color,
    dk50: Color,
    dk10: Color,
    kt50: Color,
    kt10: Color,
    fox50: Color,
    fox10: Color,
    ns50: Color,
    ns10: Color,
    kdf50: Color,
    kdf10: Color,
    hle50: Color,
    hle10: Color,
) {
    var purple50 by mutableStateOf(purple50)
        private set
    var purple100 by mutableStateOf(purple100)
        private set
    var purple10 by mutableStateOf(purple10)
        private set
    var sky50 by mutableStateOf(sky50)
        private set
    var sky10 by mutableStateOf(sky10)
        private set
    var success by mutableStateOf(success)
        private set
    var info by mutableStateOf(info)
        private set
    var warning by mutableStateOf(warning)
        private set
    var error by mutableStateOf(error)
        private set
    var black by mutableStateOf(black)
        private set
    var gray900 by mutableStateOf(gray900)
        private set
    var gray800 by mutableStateOf(gray800)
        private set
    var gray700 by mutableStateOf(gray700)
        private set
    var gray600 by mutableStateOf(gray600)
        private set
    var gray500 by mutableStateOf(gray500)
        private set
    var gray400 by mutableStateOf(gray400)
        private set
    var gray300 by mutableStateOf(gray300)
        private set
    var gray200 by mutableStateOf(gray200)
        private set
    var gray100 by mutableStateOf(gray100)
        private set
    var white by mutableStateOf(white)
        private set
    var t50 by mutableStateOf(t50)
        private set
    var t10 by mutableStateOf(t10)
        private set
    var gen50 by mutableStateOf(gen50)
        private set
    var gen10 by mutableStateOf(gen10)
        private set
    var bro50 by mutableStateOf(bro50)
        private set
    var bro10 by mutableStateOf(bro10)
        private set
    var drx50 by mutableStateOf(drx50)
        private set
    var drx10 by mutableStateOf(drx10)
        private set
    var dk50 by mutableStateOf(dk50)
        private set
    var dk10 by mutableStateOf(dk10)
        private set
    var kt50 by mutableStateOf(kt50)
        private set
    var kt10 by mutableStateOf(kt10)
        private set
    var fox50 by mutableStateOf(fox50)
        private set
    var fox10 by mutableStateOf(fox10)
        private set
    var ns50 by mutableStateOf(ns50)
        private set
    var ns10 by mutableStateOf(ns10)
        private set
    var kdf50 by mutableStateOf(kdf50)
        private set
    var kdf10 by mutableStateOf(kdf10)
        private set
    var hle50 by mutableStateOf(hle50)
        private set
    var hle10 by mutableStateOf(hle10)
        private set

    fun copy(): WableColors = WableColors(
        purple50,
        purple100,
        purple10,
        sky50,
        sky10,
        success,
        info,
        warning,
        error,
        black,
        gray900,
        gray800,
        gray700,
        gray600,
        gray500,
        gray400,
        gray300,
        gray200,
        gray100,
        white,
        t50,
        t10,
        gen50,
        gen10,
        bro50,
        bro10,
        drx50,
        drx10,
        dk50,
        dk10,
        kt50,
        kt10,
        fox50,
        fox10,
        ns50,
        ns10,
        kdf50,
        kdf10,
        hle50,
        hle10,
    )

    fun update(other: WableColors) {
        purple50 = other.purple50
        purple100 = other.purple100
        purple10 = other.purple10
        sky50 = other.sky50
        sky10 = other.sky10
        success = other.success
        info = other.info
        warning = other.warning
        error = other.error
        black = other.black
        gray900 = other.gray900
        gray800 = other.gray800
        gray700 = other.gray700
        gray600 = other.gray600
        gray500 = other.gray500
        gray400 = other.gray400
        gray300 = other.gray300
        gray200 = other.gray200
        gray100 = other.gray100
        white = other.white
        t50 = other.t50
        t10 = other.t10
        gen50 = other.gen50
        gen10 = other.gen10
        bro50 = other.bro50
        bro10 = other.bro10
        drx50 = other.drx50
        drx10 = other.drx10
        dk50 = other.dk50
        dk10 = other.dk10
        kt50 = other.kt50
        kt10 = other.kt10
        fox50 = other.fox50
        fox10 = other.fox10
        ns50 = other.ns50
        ns10 = other.ns10
        kdf50 = other.kdf50
        kdf10 = other.kdf10
        hle50 = other.hle50
        hle10 = other.hle10
    }
}

fun wableLightColors(
    purple50: Color = Purple50,
    purple100: Color = Purple100,
    purple10: Color = Purple10,
    sky50: Color = Sky50,
    sky10: Color = Sky10,
    success: Color = Success,
    info: Color = Info,
    warning: Color = Warning,
    error: Color = Error,
    black: Color = Black,
    gray900: Color = Gray900,
    gray800: Color = Gray800,
    gray700: Color = Gray700,
    gray600: Color = Gray600,
    gray500: Color = Gray500,
    gray400: Color = Gray400,
    gray300: Color = Gray300,
    gray200: Color = Gray200,
    gray100: Color = Gray100,
    white: Color = White,
    t50: Color = T50,
    t10: Color = T10,
    gen50: Color = Gen50,
    gen10: Color = Gen10,
    bro50: Color = Bro50,
    bro10: Color = Bro10,
    drx50: Color = Drx50,
    drx10: Color = Drx10,
    dk50: Color = Dk50,
    dk10: Color = Dk10,
    kt50: Color = Kt50,
    kt10: Color = Kt10,
    fox50: Color = Fox50,
    fox10: Color = Fox10,
    ns50: Color = Ns50,
    ns10: Color = Ns10,
    kdf50: Color = Kdf50,
    kdf10: Color = Kdf10,
    hle50: Color = Hle50,
    hle10: Color = Hle10,
) = WableColors(
    purple50,
    purple100,
    purple10,
    sky50,
    sky10,
    success,
    info,
    warning,
    error,
    black,
    gray900,
    gray800,
    gray700,
    gray600,
    gray500,
    gray400,
    gray300,
    gray200,
    gray100,
    white,
    t50,
    t10,
    gen50,
    gen10,
    bro50,
    bro10,
    drx50,
    drx10,
    dk50,
    dk10,
    kt50,
    kt10,
    fox50,
    fox10,
    ns50,
    ns10,
    kdf50,
    kdf10,
    hle50,
    hle10,
)
