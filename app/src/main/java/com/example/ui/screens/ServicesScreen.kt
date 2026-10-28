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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.RepairTicket
import com.example.model.ServiceItem
import com.example.model.TicketStatus
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.CyanDark
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.StatusEmerald
import com.example.viewmodel.CinexViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesScreen(
    viewModel: CinexViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedSubTab by remember { mutableIntStateOf(0) } // 0: Services, 1: Tracker
    val tickets by viewModel.tickets.collectAsState()
    val services = remember { viewModel.services }
    val searchTicketQuery by viewModel.searchTicketQuery.collectAsState()
    val isBookingOpen by viewModel.isBookingDialogOpen.collectAsState()
    val preselectedService by viewModel.preselectedService.collectAsState()

    // Form inputs for booking
    var bookName by remember { mutableStateOf("") }
    var bookPhone by remember { mutableStateOf("") }
    var bookDevice by remember { mutableStateOf("") }
    var bookIssue by remember { mutableStateOf("") }

    // If preselected service changes, pre-fill issue
    LaunchedEffect(preselectedService) {
        preselectedService?.let {
            if (bookIssue.isBlank()) {
                bookIssue = "Need assistance with: ${it.title}"
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("services_screen")
    ) {
        // Tab Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                Text(
                    text = "Repair & IT Engineering Lab",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Chip-level motherboard repair, CCTV security & fast diagnostics",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(10.dp))

                TabRow(
                    selectedTabIndex = selectedSubTab,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = CyanPrimary,
                    modifier = Modifier.clip(RoundedCornerShape(10.dp))
                ) {
                    Tab(
                        selected = selectedSubTab == 0,
                        onClick = { selectedSubTab = 0 },
                        text = { Text("Services & Pricing", fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedSubTab == 1,
                        onClick = { selectedSubTab = 1 },
                        text = { Text("Track Repair (${tickets.size})", fontWeight = FontWeight.Bold) }
                    )
                }
            }
        }

        if (selectedSubTab == 0) {
            // Service List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    // Fast Booking Callout Banner
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Handyman,
                                contentDescription = null,
                                tint = CyanPrimary,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Have a Broken Device?",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = "Book an express diagnostic slot at Santa Plaza, Tezpur.",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                            }
                            Button(
                                onClick = { viewModel.openBookingDialog() },
                                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color(0xFF003544)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Book Now", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                items(services) { service ->
                    ServiceItemCard(
                        service = service,
                        onBookClick = { viewModel.openBookingDialog(service) },
                        onWhatsAppClick = {
                            viewModel.launchWhatsApp(
                                context,
                                "Hello Cinex Computers, I want to inquire about: ${service.title}. My device needs servicing."
                            )
                        }
                    )
                }
            }
        } else {
            // Repair Status Tracker
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Live Diagnostic Tracker",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Track your laptop, desktop, or CCTV service progress from intake to pickup.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = searchTicketQuery,
                        onValueChange = { viewModel.onSearchTicketQueryChange(it) },
                        label = { Text("Enter Ticket ID (e.g. CX-78401)") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (searchTicketQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.onSearchTicketQueryChange("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("tracker_search_field"),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                item {
                    Text(
                        text = "Quick Demo Tickets:",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(tickets) { ticket ->
                            FilterChip(
                                selected = searchTicketQuery.equals(ticket.ticketId, ignoreCase = true),
                                onClick = { viewModel.onSearchTicketQueryChange(ticket.ticketId) },
                                label = { Text("${ticket.ticketId} • ${ticket.status.title}") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CyanPrimary.copy(alpha = 0.2f),
                                    selectedLabelColor = CyanPrimary
                                )
                            )
                        }
                    }
                }

                val filteredTickets = if (searchTicketQuery.isBlank()) {
                    tickets
                } else {
                    tickets.filter {
                        it.ticketId.contains(searchTicketQuery, ignoreCase = true) ||
                                it.customerName.contains(searchTicketQuery, ignoreCase = true) ||
                                it.deviceModel.contains(searchTicketQuery, ignoreCase = true)
                    }
                }

                if (filteredTickets.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SearchOff,
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "No repair ticket found for '$searchTicketQuery'",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Please check your receipt or contact Cinex Santa Plaza counter at +91 94350 82134.",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    items(filteredTickets) { ticket ->
                        TicketDetailCard(
                            ticket = ticket,
                            onCall = { viewModel.launchCall(context) },
                            onWhatsApp = {
                                viewModel.launchWhatsApp(
                                    context,
                                    "Hello Cinex Computers, checking status for Ticket ID: ${ticket.ticketId} (${ticket.deviceModel})."
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    // Service Booking Modal Bottom Sheet
    if (isBookingOpen) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.closeBookingDialog() },
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
                    Column {
                        Text(
                            text = "Book Repair Diagnostic",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Santa Plaza Center, Tezpur",
                            fontSize = 11.sp,
                            color = CyanPrimary
                        )
                    }
                    IconButton(onClick = { viewModel.closeBookingDialog() }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = bookName,
                    onValueChange = { bookName = it },
                    label = { Text("Your Name") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("book_name_field"),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = bookPhone,
                    onValueChange = { bookPhone = it },
                    label = { Text("Phone Number") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("book_phone_field"),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = bookDevice,
                    onValueChange = { bookDevice = it },
                    label = { Text("Device Model (e.g. Asus TUF / HP Pavilion / CCTV)") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("book_device_field"),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = bookIssue,
                    onValueChange = { bookIssue = it },
                    label = { Text("Issue Description (e.g. No display, liquid spill, slow)") },
                    minLines = 2,
                    maxLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("book_issue_field"),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val name = if (bookName.isBlank()) "Customer" else bookName
                        val phone = if (bookPhone.isBlank()) "9854000000" else bookPhone
                        val device = if (bookDevice.isBlank()) "Laptop / PC" else bookDevice
                        val issue = if (bookIssue.isBlank()) "Diagnostic inspection required" else bookIssue
                        val est = preselectedService?.startingPrice ?: 1200L

                        viewModel.submitBooking(name, phone, device, issue, est)
                        selectedSubTab = 1 // Switch to tracker tab
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("submit_booking_btn"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color(0xFF003544))
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Confirm Appointment & Generate Ticket", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ServiceItemCard(
    service: ServiceItem,
    onBookClick: () -> Unit,
    onWhatsAppClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    if (service.isPopular) {
                        Text(
                            text = "POPULAR IN TEZPUR",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentAmber
                        )
                    }
                    Text(
                        text = service.title,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Starts at",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "₹${"%,d".format(service.startingPrice)}",
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp,
                        color = CyanPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = service.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Badges: Turnaround and Warranty
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(12.dp), tint = CyanPrimary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(service.turnaround, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Verified, contentDescription = null, modifier = Modifier.size(12.dp), tint = StatusEmerald)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(service.warranty, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onWhatsAppClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Message, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("WhatsApp", fontSize = 11.sp)
                }
                Button(
                    onClick = onBookClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color(0xFF003544))
                ) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Book Slot", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun TicketDetailCard(
    ticket: RepairTicket,
    onCall: () -> Unit,
    onWhatsApp: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(CyanPrimary)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = ticket.ticketId,
                            color = Color(0xFF003544),
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = ticket.deviceModel,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = "Est. ₹${ticket.estimatedCost}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = AccentAmber
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Reported Issue: ${ticket.issueReported}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Stepper Visualizer
            StatusTimeline(currentStatus = ticket.status)

            Spacer(modifier = Modifier.height(14.dp))

            // Notes Box
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "TECHNICIAN LAB NOTE",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = ticket.notes,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onCall,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Call Desk", fontSize = 11.sp)
                }
                Button(
                    onClick = onWhatsApp,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = StatusEmerald)
                ) {
                    Icon(Icons.Default.Message, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Chat Updates", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun StatusTimeline(currentStatus: TicketStatus) {
    val steps = TicketStatus.values()
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            steps.forEachIndexed { index, status ->
                val isCompleted = status.stepIndex <= currentStatus.stepIndex
                val isCurrent = status == currentStatus

                Box(
                    modifier = Modifier
                        .size(if (isCurrent) 16.dp else 12.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isCurrent -> CyanPrimary
                                isCompleted -> StatusEmerald
                                else -> Color(0xFF475569)
                            }
                        )
                )

                if (index < steps.size - 1) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(2.dp)
                            .background(
                                if (steps[index + 1].stepIndex <= currentStatus.stepIndex)
                                    StatusEmerald
                                else
                                    Color(0xFF334155)
                            )
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = currentStatus.title,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = CyanPrimary
            )
            Text(
                text = "Step ${currentStatus.stepIndex + 1} of 6",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = currentStatus.description,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
