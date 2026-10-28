package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CustomPcBuild
import com.example.model.PartType
import com.example.model.PcPart
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.CyanDark
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.StatusEmerald
import com.example.viewmodel.CinexViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PcBuilderScreen(
    viewModel: CinexViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val build by viewModel.customBuild.collectAsState()
    val activePickerType by viewModel.activePartPickerType.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("pc_builder_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Studio Header
        item {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "Custom PC Rig Studio",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Build, configure & quote your dream machine in Tezpur",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Live Build Summary & Stats Card
        item {
            BuildMetricsCard(
                build = build,
                onShare = { viewModel.shareCustomBuild(context) },
                onReserve = {
                    val msg = buildString {
                        appendLine("Hello Cinex Computers, I configured this custom PC build on your app:")
                        build.cpu?.let { appendLine("• CPU: ${it.name}") }
                        build.gpu?.let { appendLine("• GPU: ${it.name}") }
                        build.ram?.let { appendLine("• RAM: ${it.name}") }
                        build.storage?.let { appendLine("• SSD: ${it.name}") }
                        build.motherboard?.let { appendLine("• MB: ${it.name}") }
                        build.psu?.let { appendLine("• PSU: ${it.name}") }
                        build.case?.let { appendLine("• Case: ${it.name}") }
                        build.cooler?.let { appendLine("• Cooler: ${it.name}") }
                        appendLine("Estimated Total: ₹${"%,d".format(build.totalPrice)}")
                        appendLine("Please confirm component availability and assembly schedule at Santa Plaza.")
                    }
                    viewModel.launchWhatsApp(context, msg)
                }
            )
        }

        // Preset Quick Builders
        item {
            Text(
                text = "Popular Tested Builds",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PresetChip(
                    label = "1080p Budget (~₹42K)",
                    onClick = { viewModel.loadPresetBuild("Budget") },
                    modifier = Modifier.weight(1f)
                )
                PresetChip(
                    label = "Sweetspot 1440p (~₹95K)",
                    onClick = { viewModel.loadPresetBuild("Standard") },
                    modifier = Modifier.weight(1f)
                )
                PresetChip(
                    label = "Apex 4K Battlestation",
                    onClick = { viewModel.loadPresetBuild("Apex") },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Parts Configurator Slots
        item {
            Text(
                text = "Components Selection",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        items(PartType.values()) { partType ->
            val selectedPart = when (partType) {
                PartType.PROCESSOR -> build.cpu
                PartType.MOTHERBOARD -> build.motherboard
                PartType.GRAPHICS_CARD -> build.gpu
                PartType.RAM -> build.ram
                PartType.STORAGE -> build.storage
                PartType.POWER_SUPPLY -> build.psu
                PartType.CABINET -> build.case
                PartType.COOLER -> build.cooler
            }

            PartSlotRow(
                partType = partType,
                selectedPart = selectedPart,
                onSlotClick = { viewModel.openPartPicker(partType) }
            )
        }

        // Assembly and Guarantee Notice
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Handyman,
                        contentDescription = null,
                        tint = CyanPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Professional Cinex Assembly Included",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Every rig undergoes 3-hour FurMark and Cinebench thermal stress testing before delivery.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }

    // Part Picker Bottom Sheet
    if (activePickerType != null) {
        val type = activePickerType!!
        val options = remember(type) {
            viewModel.repository.pcPartsCatalog.filter { it.type == type }
        }

        ModalBottomSheet(
            onDismissRequest = { viewModel.closePartPicker() },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 32.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Select ${type.title}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = { viewModel.closePartPicker() }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(options) { part ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.selectPart(part) }
                                .testTag("part_option_${part.id}"),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    if (part.isRecommended) {
                                        Text(
                                            text = "CINEX RECOMMENDED",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = CyanPrimary
                                        )
                                    }
                                    Text(
                                        text = part.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = part.specs,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    if (part.wattage > 0) {
                                        Text(
                                            text = "Power: ~${part.wattage}W",
                                            fontSize = 10.sp,
                                            color = AccentAmber
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "₹${"%,d".format(part.price)}",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 15.sp,
                                        color = CyanPrimary
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    FilledTonalButton(
                                        onClick = { viewModel.selectPart(part) },
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Select", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BuildMetricsCard(
    build: CustomPcBuild,
    onShare: () -> Unit,
    onReserve: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0F1E36),
                            Color(0xFF091222)
                        )
                    )
                )
                .border(1.dp, CyanPrimary.copy(alpha = 0.35f), RoundedCornerShape(18.dp))
                .padding(18.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ESTIMATED TOTAL (INR)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyanPrimary,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "₹${"%,d".format(build.totalPrice)}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF003544))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("POWER DRAW", fontSize = 9.sp, color = CyanPrimary, fontWeight = FontWeight.Bold)
                            Text("~${build.totalWattage}W", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Gaming FPS Score Estimate
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.05f))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    FpsIndicator(game = "CS2 / Valorant", fps = "${(build.gamingScore * 4.2).toInt()}+ FPS")
                    FpsIndicator(game = "GTA V / Apex", fps = "${(build.gamingScore * 1.8).toInt()}+ FPS")
                    FpsIndicator(game = "Cyberpunk RayTraced", fps = "${(build.gamingScore * 0.95).toInt()} FPS")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onShare,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("share_quote_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(CyanPrimary, Color.White)))
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Share Quote", fontSize = 12.sp)
                    }

                    Button(
                        onClick = onReserve,
                        modifier = Modifier
                            .weight(1.3f)
                            .testTag("reserve_quote_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = StatusEmerald)
                    ) {
                        Icon(Icons.Default.Message, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Reserve on WhatsApp", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun FpsIndicator(game: String, fps: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = game, fontSize = 10.sp, color = Color(0xFF94A3B8))
        Text(text = fps, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CyanPrimary)
    }
}

@Composable
private fun PresetChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 2.dp
    ) {
        Box(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun PartSlotRow(
    partType: PartType,
    selectedPart: PcPart?,
    onSlotClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onSlotClick)
            .testTag("slot_${partType.name.lowercase()}"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(CyanPrimary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (partType) {
                        PartType.PROCESSOR -> Icons.Default.Memory
                        PartType.GRAPHICS_CARD -> Icons.Default.VideogameAsset
                        PartType.MOTHERBOARD -> Icons.Default.DeveloperBoard
                        PartType.RAM -> Icons.Default.Straighten
                        PartType.STORAGE -> Icons.Default.Storage
                        PartType.POWER_SUPPLY -> Icons.Default.Bolt
                        PartType.CABINET -> Icons.Default.Inbox
                        PartType.COOLER -> Icons.Default.AcUnit
                    },
                    contentDescription = null,
                    tint = CyanPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = partType.title,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = selectedPart?.name ?: "Tap to choose ${partType.title}...",
                    fontWeight = if (selectedPart != null) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 13.sp,
                    color = if (selectedPart != null) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    maxLines = 1
                )
                if (selectedPart != null) {
                    Text(
                        text = "₹${"%,d".format(selectedPart.price)}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CyanPrimary
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
