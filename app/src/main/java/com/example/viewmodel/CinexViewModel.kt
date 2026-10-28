package com.example.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CinexRepository
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

enum class AppNavTab(val label: String, val iconKey: String) {
    HOME("Home", "home"),
    PC_BUILDER("PC Builder", "build"),
    SERVICES("Services", "service"),
    CATALOG("Store", "store"),
    CONTACT("Location", "location")
}

class CinexViewModel(
    val repository: CinexRepository = CinexRepository()
) : ViewModel() {

    val storeDetails = repository.storeDetails

    // Navigation Tab
    private val _currentTab = MutableStateFlow(AppNavTab.HOME)
    val currentTab: StateFlow<AppNavTab> = _currentTab.asStateFlow()

    fun selectTab(tab: AppNavTab) {
        _currentTab.value = tab
    }

    // Catalog state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow(ProductCategory.ALL)
    val selectedCategory: StateFlow<ProductCategory> = _selectedCategory.asStateFlow()

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()

    val filteredProducts = combine(_searchQuery, _selectedCategory) { query, category ->
        repository.products.filter { product ->
            val matchesCategory = (category == ProductCategory.ALL) || (product.category == category)
            val matchesQuery = query.isBlank() ||
                    product.name.contains(query, ignoreCase = true) ||
                    product.brand.contains(query, ignoreCase = true) ||
                    product.specs.any { it.contains(query, ignoreCase = true) }
            matchesCategory && matchesQuery
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelect(category: ProductCategory) {
        _selectedCategory.value = category
    }

    fun openProductDetails(product: Product) {
        _selectedProduct.value = product
    }

    fun closeProductDetails() {
        _selectedProduct.value = null
    }

    // Custom PC Builder State
    private val _customBuild = MutableStateFlow(
        CustomPcBuild(
            cpu = repository.pcPartsCatalog.find { it.id == "cpu_1" },
            motherboard = repository.pcPartsCatalog.find { it.id == "mb_1" },
            gpu = repository.pcPartsCatalog.find { it.id == "gpu_1" },
            ram = repository.pcPartsCatalog.find { it.id == "ram_2" },
            storage = repository.pcPartsCatalog.find { it.id == "ssd_1" },
            psu = repository.pcPartsCatalog.find { it.id == "psu_1" },
            case = repository.pcPartsCatalog.find { it.id == "case_1" },
            cooler = repository.pcPartsCatalog.find { it.id == "cool_1" }
        )
    )
    val customBuild: StateFlow<CustomPcBuild> = _customBuild.asStateFlow()

    private val _activePartPickerType = MutableStateFlow<PartType?>(null)
    val activePartPickerType: StateFlow<PartType?> = _activePartPickerType.asStateFlow()

    fun openPartPicker(partType: PartType) {
        _activePartPickerType.value = partType
    }

    fun closePartPicker() {
        _activePartPickerType.value = null
    }

    fun selectPart(part: PcPart) {
        val current = _customBuild.value
        _customBuild.value = when (part.type) {
            PartType.PROCESSOR -> current.copy(cpu = part)
            PartType.MOTHERBOARD -> current.copy(motherboard = part)
            PartType.GRAPHICS_CARD -> current.copy(gpu = part)
            PartType.RAM -> current.copy(ram = part)
            PartType.STORAGE -> current.copy(storage = part)
            PartType.POWER_SUPPLY -> current.copy(psu = part)
            PartType.CABINET -> current.copy(case = part)
            PartType.COOLER -> current.copy(cooler = part)
        }
        _activePartPickerType.value = null
    }

    fun loadPresetBuild(presetName: String) {
        when (presetName) {
            "Budget" -> {
                _customBuild.value = CustomPcBuild(
                    cpu = repository.pcPartsCatalog.find { it.id == "cpu_5" },
                    motherboard = repository.pcPartsCatalog.find { it.id == "mb_4" },
                    gpu = repository.pcPartsCatalog.find { it.id == "gpu_5" },
                    ram = repository.pcPartsCatalog.find { it.id == "ram_4" },
                    storage = repository.pcPartsCatalog.find { it.id == "ssd_3" },
                    psu = repository.pcPartsCatalog.find { it.id == "psu_3" },
                    case = repository.pcPartsCatalog.find { it.id == "case_3" },
                    cooler = repository.pcPartsCatalog.find { it.id == "cool_1" }
                )
            }
            "Apex" -> {
                _customBuild.value = CustomPcBuild(
                    cpu = repository.pcPartsCatalog.find { it.id == "cpu_3" },
                    motherboard = repository.pcPartsCatalog.find { it.id == "mb_3" },
                    gpu = repository.pcPartsCatalog.find { it.id == "gpu_3" },
                    ram = repository.pcPartsCatalog.find { it.id == "ram_3" },
                    storage = repository.pcPartsCatalog.find { it.id == "ssd_2" },
                    psu = repository.pcPartsCatalog.find { it.id == "psu_2" },
                    case = repository.pcPartsCatalog.find { it.id == "case_2" },
                    cooler = repository.pcPartsCatalog.find { it.id == "cool_2" }
                )
            }
            else -> {
                _customBuild.value = CustomPcBuild(
                    cpu = repository.pcPartsCatalog.find { it.id == "cpu_1" },
                    motherboard = repository.pcPartsCatalog.find { it.id == "mb_1" },
                    gpu = repository.pcPartsCatalog.find { it.id == "gpu_1" },
                    ram = repository.pcPartsCatalog.find { it.id == "ram_1" },
                    storage = repository.pcPartsCatalog.find { it.id == "ssd_1" },
                    psu = repository.pcPartsCatalog.find { it.id == "psu_1" },
                    case = repository.pcPartsCatalog.find { it.id == "case_1" },
                    cooler = repository.pcPartsCatalog.find { it.id == "cool_1" }
                )
            }
        }
    }

    // Services & Repair Tracking State
    val tickets = repository.tickets
    val services = repository.services
    val reviews = repository.reviews

    private val _searchTicketQuery = MutableStateFlow("")
    val searchTicketQuery: StateFlow<String> = _searchTicketQuery.asStateFlow()

    private val _isBookingDialogOpen = MutableStateFlow(false)
    val isBookingDialogOpen: StateFlow<Boolean> = _isBookingDialogOpen.asStateFlow()

    private val _preselectedService = MutableStateFlow<ServiceItem?>(null)
    val preselectedService: StateFlow<ServiceItem?> = _preselectedService.asStateFlow()

    private val _lastBookedTicketId = MutableStateFlow<String?>(null)
    val lastBookedTicketId: StateFlow<String?> = _lastBookedTicketId.asStateFlow()

    fun onSearchTicketQueryChange(query: String) {
        _searchTicketQuery.value = query
    }

    fun openBookingDialog(service: ServiceItem? = null) {
        _preselectedService.value = service
        _isBookingDialogOpen.value = true
    }

    fun closeBookingDialog() {
        _isBookingDialogOpen.value = false
        _preselectedService.value = null
    }

    fun submitBooking(name: String, phone: String, device: String, issue: String, estPrice: Long) {
        val ticketId = repository.addBooking(name, phone, device, issue, estPrice)
        _lastBookedTicketId.value = ticketId
        _searchTicketQuery.value = ticketId
        closeBookingDialog()
    }

    // Review Dialog State
    private val _isReviewDialogOpen = MutableStateFlow(false)
    val isReviewDialogOpen: StateFlow<Boolean> = _isReviewDialogOpen.asStateFlow()

    fun openReviewDialog() {
        _isReviewDialogOpen.value = true
    }

    fun closeReviewDialog() {
        _isReviewDialogOpen.value = false
    }

    fun submitReview(name: String, location: String, rating: Int, comment: String, tag: String) {
        repository.addReview(name, location, rating, comment, tag)
        closeReviewDialog()
    }

    // User Intent Triggers (WhatsApp, Phone, Maps, Share)
    fun launchCall(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${storeDetails.phoneDial}")
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Call: ${storeDetails.phoneDisplay}", Toast.LENGTH_LONG).show()
        }
    }

    fun launchWhatsApp(context: Context, customMessage: String? = null) {
        try {
            val defaultMsg = customMessage ?: "Hello Cinex Computers Tezpur, I would like to inquire about computer sales / repair service."
            val encoded = Uri.encode(defaultMsg)
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=${storeDetails.whatsappNumber}&text=$encoded")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "WhatsApp: ${storeDetails.phoneDisplay}", Toast.LENGTH_LONG).show()
        }
    }

    fun launchMaps(context: Context) {
        try {
            // Precise location in Tezpur
            val uri = Uri.parse("geo:${storeDetails.latitude},${storeDetails.longitude}?q=${storeDetails.latitude},${storeDetails.longitude}(Cinex+Computers+Tezpur)")
            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(mapIntent)
        } catch (e: Exception) {
            // Fallback to browser
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(storeDetails.googleMapsUrl))
            context.startActivity(webIntent)
        }
    }

    fun launchEmail(context: Context) {
        try {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:${storeDetails.email}")
                putExtra(Intent.EXTRA_SUBJECT, "Inquiry — Cinex Computers Tezpur")
            }
            context.startActivity(emailIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "Email: ${storeDetails.email}", Toast.LENGTH_LONG).show()
        }
    }

    fun shareCustomBuild(context: Context) {
        val build = _customBuild.value
        val text = buildString {
            appendLine("🖥️ Custom PC Build Quote — Cinex Computers, Tezpur")
            appendLine("━━━━━━━━━━━━━━━━━━━━")
            build.cpu?.let { appendLine("• CPU: ${it.name} (₹${it.price})") }
            build.motherboard?.let { appendLine("• Motherboard: ${it.name} (₹${it.price})") }
            build.gpu?.let { appendLine("• GPU: ${it.name} (₹${it.price})") }
            build.ram?.let { appendLine("• RAM: ${it.name} (₹${it.price})") }
            build.storage?.let { appendLine("• Storage: ${it.name} (₹${it.price})") }
            build.psu?.let { appendLine("• Power Supply: ${it.name} (₹${it.price})") }
            build.case?.let { appendLine("• Case: ${it.name} (₹${it.price})") }
            build.cooler?.let { appendLine("• Cooler: ${it.name} (₹${it.price})") }
            appendLine("━━━━━━━━━━━━━━━━━━━━")
            appendLine("Total Estimate: ₹${"%,d".format(build.totalPrice)}")
            appendLine("Power Required: ~${build.totalWattage}W")
            appendLine("Gaming Score: ${build.gamingScore}/100")
            appendLine("\n📍 Cinex Computers, Santa Plaza, S.C. Road, Tezpur")
            appendLine("📞 Contact: +91 94350 82134")
        }
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share Custom PC Quote")
        context.startActivity(shareIntent)
    }
}
