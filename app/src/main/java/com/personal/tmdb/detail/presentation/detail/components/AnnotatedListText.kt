package com.personal.tmdb.detail.presentation.detail.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.personal.tmdb.R
import com.personal.tmdb.core.navigation.Route

data class AnnotatedItem(
    val id: Int,
    val name: String
)

enum class AnnotationTag {
    CAST
}

@Composable
fun AnnotatedListTextWithLinks(
    modifier: Modifier = Modifier,
    annotationTag: AnnotationTag,
    titlePrefix: String? = null,
    items: List<AnnotatedItem>,
    titlePrefixStyle: SpanStyle = MaterialTheme.typography.labelLarge.toSpanStyle(),
    itemsStyle: SpanStyle = MaterialTheme.typography.bodyMedium.toSpanStyle(),
    onNavigateTo: (route: Route) -> Unit
) {
    val annotatedString by remember {
        derivedStateOf {
            buildAnnotatedString {
                titlePrefix?.let {
                    withStyle(style = titlePrefixStyle) {
                        append("$titlePrefix ")
                    }
                }
                items.fastForEachIndexed { index, item ->
                    withLink(
                        LinkAnnotation.Clickable(
                            tag = annotationTag.name,
                            styles = TextLinkStyles(style = itemsStyle),
                            linkInteractionListener = {
                                when (annotationTag) {
                                    AnnotationTag.CAST -> {
                                        onNavigateTo(
                                            Route.Person(
                                                personName = item.name,
                                                personId = item.id
                                            )
                                        )
                                    }
                                }
                            }
                        )
                    ) {
                        append(item.name)
                    }
                    if (index != items.lastIndex) {
                        withStyle(style = itemsStyle) {
                            append(", ")
                        }
                    }
                }
            }
        }
    }
    Text(
        modifier = modifier,
        text = annotatedString
    )
}

@Composable
fun AnnotatedListText(
    modifier: Modifier = Modifier,
    titlePrefix: String? = null,
    items: List<String>,
    titlePrefixStyle: SpanStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.surfaceVariant).toSpanStyle(),
    itemsStyle: SpanStyle = MaterialTheme.typography.bodyLarge.toSpanStyle()
) {
    val annotatedString by remember {
        derivedStateOf {
            buildAnnotatedString {
                titlePrefix?.let {
                    withStyle(style = titlePrefixStyle) {
                        append("$titlePrefix: ")
                    }
                }
                items.fastForEachIndexed { index, item ->
                    withStyle(style = itemsStyle) {
                        append(item)
                    }
                    if (index != items.lastIndex) {
                        withStyle(style = itemsStyle) {
                            append(", ")
                        }
                    }
                }
            }
        }
    }
    Text(
        modifier = modifier,
        text = annotatedString
    )
}

@Composable
fun AnnotatedOverflowListText(
    modifier: Modifier = Modifier,
    titlePrefix: String? = null,
    items: List<AnnotatedItem>,
    titlePrefixStyle: SpanStyle = MaterialTheme.typography.labelLarge.toSpanStyle(),
    itemsStyle: SpanStyle = MaterialTheme.typography.bodyMedium.toSpanStyle()
) {
    var showMore by rememberSaveable { mutableStateOf(false) }
    val annotatedString by remember {
        derivedStateOf {
            buildAnnotatedString {
                titlePrefix?.let {
                    withStyle(style = titlePrefixStyle) {
                        append("$titlePrefix ")
                    }
                }
                withStyle(style = itemsStyle) {
                    append(items.joinToString(", ") { it.name })
                }
            }
        }
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            modifier = Modifier.weight(1f, false),
            text = annotatedString,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult ->
                if (textLayoutResult.hasVisualOverflow) showMore = true
            }
        )
        AnimatedVisibility(visible = showMore) {
            Text(
                text = stringResource(id = R.string.more),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}