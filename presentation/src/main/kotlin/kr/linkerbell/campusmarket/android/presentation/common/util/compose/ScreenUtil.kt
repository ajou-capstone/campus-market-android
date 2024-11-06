package kr.linkerbell.campusmarket.android.presentation.common.util.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.eventObserve
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent
import kr.linkerbell.campusmarket.android.presentation.common.view.DialogScreen
import timber.log.Timber

@Composable
fun ErrorObserver(
    viewModel: BaseViewModel
) {
    var _error: ErrorEvent? by remember { mutableStateOf(null) }
    val error = _error

    when (error) {
        is ErrorEvent.Client -> {
            DialogScreen(
                title = stringResource(id = R.string.error_dialog_client_title),
                message = stringResource(id = R.string.error_dialog_client_content),
                onDismissRequest = {
                    _error = null
                }
            )
        }

        is ErrorEvent.InvalidRequest -> {
            DialogScreen(
                title = stringResource(id = R.string.error_dialog_invalid_request_title),
                message = error.exception.message,
                onDismissRequest = {
                    _error = null
                }
            )
        }

        is ErrorEvent.UnavailableServer -> {
            DialogScreen(
                title = stringResource(id = R.string.error_dialog_unavailable_server_title),
                message = stringResource(id = R.string.error_dialog_unavailable_server_content),
                onDismissRequest = {
                    _error = null
                }
            )
        }

        else -> Unit
    }

    LaunchedEffectWithLifecycle(viewModel.errorEvent) {
        viewModel.errorEvent.eventObserve { event ->
            _error = event
            Timber.d(event.exception)
            Firebase.crashlytics.recordException(event.exception)
        }
    }
}

@Composable
fun LaunchedEffectWithLifecycle(
    key1: Any?,
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1) {
        lifecycleOwner.lifecycleScope.launch(
            coroutineContext + context
        ) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }
    }
}

@Composable
fun ComposableLifecycle(
    lifeCycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onEvent: (LifecycleOwner, Lifecycle.Event) -> Unit
) {
    DisposableEffect(lifeCycleOwner) {
        val observer = LifecycleEventObserver { source, event ->
            onEvent(source, event)
        }
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
