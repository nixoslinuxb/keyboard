package com.keyboardd.ui.components

import android.view.HapticFeedbackConstants
import android.view.SoundEffectConstants
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keyboardd.ui.theme.LocalKeyboardColors
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class KeyType {
    NORMAL,
    SPECIAL,
    ACTION
}

@Composable
fun KeyButton(
    modifier: Modifier = Modifier,
    label: String? = null,
    subLabel: String? = null,
    keyType: KeyType = KeyType.NORMAL,
    keyHeight: Dp = 50.dp,
    showPopup: Boolean = true,
    hapticsEnabled: Boolean = true,
    soundEnabled: Boolean = false,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    onRepeatClick: (() -> Unit)? = null,
    content: (@Composable BoxScope.() -> Unit)? = null
) {
    val keyboardColors = LocalKeyboardColors.current
    val view = LocalView.current
    val scope = rememberCoroutineScope()

    val currentOnClick by rememberUpdatedState(onClick)
    val currentOnLongClick by rememberUpdatedState(onLongClick)
    val currentOnRepeatClick by rememberUpdatedState(onRepeatClick)

    var isPressed by remember { mutableStateOf(false) }
    var autoRepeatJob by remember { mutableStateOf<Job?>(null) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1.0f,
        animationSpec = tween(durationMillis = 60),
        label = "keyScale"
    )

    val backgroundColor = when (keyType) {
        KeyType.NORMAL -> if (isPressed) keyboardColors.keyBackground.copy(alpha = 0.8f) else keyboardColors.keyBackground
        KeyType.SPECIAL -> if (isPressed) keyboardColors.specialKeyBackground.copy(alpha = 0.8f) else keyboardColors.specialKeyBackground
        KeyType.ACTION -> if (isPressed) keyboardColors.actionKeyBackground.copy(alpha = 0.8f) else keyboardColors.actionKeyBackground
    }

    val textColor = when (keyType) {
        KeyType.NORMAL -> keyboardColors.keyTextColor
        KeyType.SPECIAL -> keyboardColors.specialKeyTextColor
        KeyType.ACTION -> keyboardColors.actionKeyTextColor
    }

    fun triggerFeedback() {
        if (hapticsEnabled) {
            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        }
        if (soundEnabled) {
            view.playSoundEffect(SoundEffectConstants.CLICK)
        }
    }

    Box(
        modifier = modifier
            .padding(horizontal = 2.dp, vertical = 3.dp)
            .height(keyHeight)
            .scale(scale)
            .shadow(
                elevation = if (isPressed) 1.dp else 2.5.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = keyboardColors.keyShadowColor,
                spotColor = keyboardColors.keyShadowColor
            )
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        triggerFeedback()

                        // Execute immediate click on touch down!
                        currentOnClick()

                        val repeatClick = currentOnRepeatClick
                        if (repeatClick != null) {
                            autoRepeatJob = scope.launch {
                                delay(400) // Initial delay before repeating
                                while (isPressed) {
                                    triggerFeedback()
                                    repeatClick()
                                    delay(60) // Auto repeat speed
                                }
                            }
                        }

                        tryAwaitRelease()
                        isPressed = false
                        autoRepeatJob?.cancel()
                        autoRepeatJob = null
                    },
                    onLongPress = {
                        val longClick = currentOnLongClick
                        if (longClick != null && currentOnRepeatClick == null) {
                            triggerFeedback()
                            longClick()
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        if (content != null) {
            content()
        } else if (label != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontSize = if (label.length > 2) 13.sp else 19.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor
                )

                if (!subLabel.isNullOrEmpty()) {
                    Text(
                        text = subLabel,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor.copy(alpha = 0.5f),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 2.dp, end = 4.dp)
                    )
                }
            }
        }

        // Key Preview Popup Bubble above key when pressed
        if (isPressed && showPopup && keyType == KeyType.NORMAL && label != null) {
            Box(
                modifier = Modifier
                    .offset(y = (-56).dp)
                    .size(width = 54.dp, height = 54.dp)
                    .shadow(4.dp, RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .background(keyboardColors.keyPopupBackground),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = keyboardColors.keyPopupTextColor
                )
            }
        }
    }
}
