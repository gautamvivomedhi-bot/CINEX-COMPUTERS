package com.example.model

enum class ProductCategory(val label: String) {
    ALL("All"),
    LAPTOPS("Laptops"),
    PC_PARTS("PC Parts"),
    DESKTOPS("Custom PCs"),
    CCTV("CCTV & Security"),
    MONITORS("Monitors"),
    ACCESSORIES("Accessories")
}

data class Product(
    val id: String,
    val name: String,
    val category: ProductCategory,
    val brand: String,
    val price: Long,
    val originalPrice: Long,
    val rating: Float,
    val reviewCount: Int,
    val inStock: Boolean,
    val isFeatured: Boolean = false,
    val badge: String? = null,
    val specs: List<String>,
    val description: String,
    val iconKey: String = "computer"
)

enum class ServiceCategory(val label: String) {
    REPAIR("Hardware Repair"),
    UPGRADES("Upgrades & Boost"),
    CCTV("CCTV Security"),
    NETWORKING("AMC & Networking"),
    SOFTWARE("OS & Software")
}

data class ServiceItem(
    val id: String,
    val title: String,
    val category: ServiceCategory,
    val startingPrice: Long,
    val turnaround: String,
    val warranty: String,
    val isPopular: Boolean = false,
    val description: String,
    val features: List<String>
)

enum class TicketStatus(val title: String, val stepIndex: Int, val description: String) {
    RECEIVED("Device Received", 0, "Inspected at Santa Plaza center & tagged."),
    DIAGNOSING("Under Diagnostic", 1, "Chip-level motherboard & components diagnosis in lab."),
    PARTS_WAITING("Awaiting Approval", 2, "Quotation shared with customer for parts approval."),
    REPAIRING("Repair & Soldering", 3, "Replacement and chip-level precision repair."),
    TESTING("Benchmark & QA Testing", 4, "Stress testing thermals, stability and system health."),
    READY_FOR_PICKUP("Ready for Pickup", 5, "Device cleaned, tested, and ready at counter.")
}

data class RepairTicket(
    val ticketId: String,
    val customerName: String,
    val phone: String,
    val deviceModel: String,
    val issueReported: String,
    val receivedDate: String,
    val estimatedDelivery: String,
    val status: TicketStatus,
    val estimatedCost: Long,
    val notes: String
)

enum class PartType(val title: String, val iconKey: String) {
    PROCESSOR("Processor (CPU)", "cpu"),
    MOTHERBOARD("Motherboard", "motherboard"),
    GRAPHICS_CARD("Graphics Card (GPU)", "gpu"),
    RAM("Memory (RAM)", "ram"),
    STORAGE("Fast Storage (SSD)", "storage"),
    POWER_SUPPLY("Power Supply (PSU)", "power"),
    CABINET("Case / Cabinet", "case"),
    COOLER("CPU Cooler", "cooling")
}

data class PcPart(
    val id: String,
    val type: PartType,
    val name: String,
    val brand: String,
    val price: Long,
    val wattage: Int,
    val benchmarkScore: Int, // Relative score for calculation
    val specs: String,
    val isRecommended: Boolean = false
)

data class CustomPcBuild(
    val cpu: PcPart? = null,
    val motherboard: PcPart? = null,
    val gpu: PcPart? = null,
    val ram: PcPart? = null,
    val storage: PcPart? = null,
    val psu: PcPart? = null,
    val case: PcPart? = null,
    val cooler: PcPart? = null
) {
    val totalPrice: Long
        get() = listOfNotNull(cpu, motherboard, gpu, ram, storage, psu, case, cooler).sumOf { it.price }

    val totalWattage: Int
        get() = listOfNotNull(cpu, motherboard, gpu, ram, storage, psu, case, cooler).sumOf { it.wattage } + 50 // overhead

    val gamingScore: Int
        get() {
            val cpuScore = cpu?.benchmarkScore ?: 0
            val gpuScore = gpu?.benchmarkScore ?: 0
            return ((gpuScore * 0.7) + (cpuScore * 0.3)).toInt()
        }

    val isComplete: Boolean
        get() = cpu != null && motherboard != null && gpu != null && ram != null && storage != null && psu != null && case != null
}

data class CustomerReview(
    val id: String,
    val author: String,
    val location: String,
    val rating: Int,
    val date: String,
    val comment: String,
    val verifiedPurchase: Boolean = true,
    val serviceTag: String
)

data class StoreDetails(
    val name: String = "Cinex Computers",
    val proprietor: String = "Gautam Kumar Medhi",
    val address: String = "Santa Plaza, S.C. Road (Sarat Chandra Road), Tezpur, Assam 784001",
    val landmark: String = "Near Axis Bank & S.C. Road Main Market",
    val latitude: Double = 26.6411988,
    val longitude: Double = 92.7934238,
    val phoneDisplay: String = "+91 94350 82134",
    val phoneDial: String = "+919435082134",
    val altPhone: String = "+91 3712 232145",
    val whatsappNumber: String = "919435082134",
    val email: String = "gautamvivomedhi@gmail.com",
    val googleMapsUrl: String = "https://www.google.com/maps/place/Cinex+Computers/@26.6411988,92.7934238,17z",
    val weekdayHours: String = "10:00 AM – 8:30 PM",
    val sundayHours: String = "11:00 AM – 5:00 PM",
    val yearsInService: Int = 16,
    val satisfactionRate: String = "99.4%"
)
