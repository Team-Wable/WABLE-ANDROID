package com.teamwable.onboarding.profile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import com.teamwable.designsystem.extension.modifier.noRippleClickable
import com.teamwable.designsystem.type.ProfileImageType

@Composable
fun ProfileImagePicker(
    selectedImageUri: String?,
    currentImage: ProfileImageType,
    onRandomImageChange: (ProfileImageType) -> Unit,
    onProfilePlusBtnClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        GlideImage(
            imageModel = { selectedImageUri ?: currentImage.image },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
            ),
            modifier = Modifier
                .aspectRatio(1f)
                .clip(CircleShape),
            previewPlaceholder = painterResource(id = com.teamwable.common.R.drawable.ic_share_profile_img_blue),
        )
        Image(
            painter = painterResource(id = com.teamwable.common.R.drawable.ic_sign_up_profile_change_btn),
            contentDescription = "Change Profile Image",
            modifier = Modifier
                .align(Alignment.BottomStart)
                .noRippleClickable {
                    val newImage = ProfileImageType.entries
                        .filter { it != currentImage }
                        .random()
                    onRandomImageChange(newImage)
                },
        )
        Image(
            painter = painterResource(id = com.teamwable.common.R.drawable.ic_sign_up_profile_plus_btn),
            contentDescription = "Plus Profile Image",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .noRippleClickable {
                    onProfilePlusBtnClick()
                },
        )
    }
}
