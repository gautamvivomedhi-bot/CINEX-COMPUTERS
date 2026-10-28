package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CinexRepository {

    val storeDetails = StoreDetails()

    private val _tickets = MutableStateFlow(
        listOf(
            RepairTicket(
                ticketId = "CX-78401",
                customerName = "Bikash Borah",
                phone = "98540*****",
                deviceModel = "Asus ROG Zephyrus G14",
                issueReported = "Thermal throttling, overheating & fan rattling noise",
                receivedDate = "27 Oct 2026",
                estimatedDelivery = "29 Oct 2026",
                status = TicketStatus.TESTING,
                estimatedCost = 1450,
                notes = "Thermal Grizzly Kryonaut applied. Both blower fans cleaned & lubricated. Stress test stable at 72°C."
            ),
            RepairTicket(
                ticketId = "CX-78402",
                customerName = "Ananya Sharma",
                phone = "94351*****",
                deviceModel = "Dell Inspiron 15 5500",
                issueReported = "Screen flickering & broken left hinge assembly",
                receivedDate = "28 Oct 2026",
                estimatedDelivery = "30 Oct 2026",
                status = TicketStatus.PARTS_WAITING,
                estimatedCost = 3800,
                notes = "Awaiting customer approval for OEM 1080p IPS panel replacement and reinforced hinge fabrication."
            ),
            RepairTicket(
                ticketId = "CX-78403",
                customerName = "Pranjal Saikia",
                phone = "70023*****",
                deviceModel = "Custom Gaming Rig (Ryzen 5)",
                issueReported = "No post, DRAM debug LED glowing orange",
                receivedDate = "28 Oct 2026",
                estimatedDelivery = "28 Oct 2026",
                status = TicketStatus.READY_FOR_PICKUP,
                estimatedCost = 850,
                notes = "BIOS update applied via flashback button for Ryzen 5000 compatibility; memory training resolved."
            )
        )
    )
    val tickets: StateFlow<List<RepairTicket>> = _tickets.asStateFlow()

    private val _reviews = MutableStateFlow(
        listOf(
            CustomerReview(
                id = "rev_1",
                author = "Debojit Kalita",
                location = "Mission Chariali, Tezpur",
                rating = 5,
                date = "2 days ago",
                comment = "Best computer shop in Tezpur! Gautam da built my dream RTX 4070 gaming rig at lower price than online retail. Cable management is art.",
                serviceTag = "Custom PC Build"
            ),
            CustomerReview(
                id = "rev_2",
                author = "Dr. Manasjyoti Hazarika",
                location = "Tribeni, Tezpur",
                rating = 5,
                date = "1 week ago",
                comment = "Cinex installed complete Hikvision 8-camera setup for our clinic. Crystal clear day/night vision and phone streaming configured within 4 hours.",
                serviceTag = "Hikvision CCTV"
            ),
            CustomerReview(
                id = "rev_3",
                author = "Priyanka Baruah",
                location = "Near Tezpur College",
                rating = 5,
                date = "2 weeks ago",
                comment = "My HP laptop screen broke before semester exams. Other shops told 10 days, Cinex fixed it in 24 hours with genuine warranty.",
                serviceTag = "Laptop Screen Repair"
            ),
            CustomerReview(
                id = "rev_4",
                author = "Rahul Goswami",
                location = "Paruwa, Tezpur",
                rating = 5,
                date = "3 weeks ago",
                comment = "Upgraded my old slow office desktop with 512GB NVMe and 16GB RAM. Boots in 7 seconds now. Highly trustworthy team in Santa Plaza.",
                serviceTag = "Speed Upgrade"
            )
        )
    )
    val reviews: StateFlow<List<CustomerReview>> = _reviews.asStateFlow()

    val products = listOf(
        Product(
            id = "prod_1",
            name = "Cinex Apex Gaming PC — Ryzen 7 7800X3D + RTX 4070 Ti Super",
            category = ProductCategory.DESKTOPS,
            brand = "Cinex Built",
            price = 168000,
            originalPrice = 185000,
            rating = 4.9f,
            reviewCount = 28,
            inStock = true,
            isFeatured = true,
            badge = "Apex Tier",
            specs = listOf("Ryzen 7 7800X3D", "RTX 4070 Ti Super 16GB", "32GB DDR5 6000MHz RGB", "2TB Gen4 NVMe", "360mm ARGB AIO Liquid Cooler", "850W 80+ Gold PSU"),
            description = "Ultimate 1440p & 4K gaming powerhouse tuned specifically for ultra ray-tracing, competitive esports, and high-resolution video rendering."
        ),
        Product(
            id = "prod_2",
            name = "Cinex Esports Fighter — Core i5-14400F + RTX 4060",
            category = ProductCategory.DESKTOPS,
            brand = "Cinex Built",
            price = 69999,
            originalPrice = 77000,
            rating = 4.8f,
            reviewCount = 42,
            inStock = true,
            isFeatured = true,
            badge = "Value Champ",
            specs = listOf("Intel Core i5-14400F 10-Core", "Nvidia RTX 4060 8GB GDDR6", "16GB DDR5 Dual Channel", "1TB M.2 NVMe SSD", "650W Bronze PSU", "Tempered Glass Case"),
            description = "The undisputed price-to-performance champion for Valorant, GTA V, CS2 (300+ FPS), and smooth 1080p high settings gaming."
        ),
        Product(
            id = "prod_3",
            name = "ASUS ROG Strix G16 (2026) 165Hz Gaming Laptop",
            category = ProductCategory.LAPTOPS,
            brand = "ASUS ROG",
            price = 119990,
            originalPrice = 134990,
            rating = 4.9f,
            reviewCount = 35,
            inStock = true,
            isFeatured = true,
            badge = "Official Dealer",
            specs = listOf("13th Gen Intel Core i7-13650HX", "RTX 4060 8GB 140W TGP", "16GB DDR5 (Upgradable)", "1TB PCIe Gen4 SSD", "16-inch FHD+ 165Hz 100% sRGB", "ROG Intelligent Cooling"),
            description = "Authorized ASUS stock with official 1-year brand on-site warranty in Tezpur. Free gaming backpack and Cinex precision mousepad."
        ),
        Product(
            id = "prod_4",
            name = "Lenovo LOQ 15.6\" FHD 144Hz Ryzen 7",
            category = ProductCategory.LAPTOPS,
            brand = "Lenovo",
            price = 78990,
            originalPrice = 89990,
            rating = 4.7f,
            reviewCount = 19,
            inStock = true,
            isFeatured = false,
            badge = "Student Special",
            specs = listOf("AMD Ryzen 7 7435HS", "RTX 4050 6GB Dedicated", "16GB DDR5 4800MHz", "512GB SSD M.2", "15.6\" 144Hz G-Sync display", "White Backlit Keyboard"),
            description = "Perfect laptop for engineering, computer science students and gamers seeking supreme durability and thermal efficiency."
        ),
        Product(
            id = "prod_5",
            name = "Hikvision 4-Camera 5MP ColorVu Audio IP Security Kit",
            category = ProductCategory.CCTV,
            brand = "Hikvision",
            price = 22500,
            originalPrice = 28000,
            rating = 5.0f,
            reviewCount = 64,
            inStock = true,
            isFeatured = true,
            badge = "Cinex Top Seller",
            specs = listOf("4x 5MP ColorVu Full-Time Color Cameras", "Built-in High Sensitivity Mic (Audio)", "4-Channel Smart NVR with PoE", "2TB Seagate SkyHawk Surveillance HDD", "Hik-Connect Mobile App Live View", "Free Site Installation in Tezpur"),
            description = "Authorized Hikvision Tezpur partner. Provides 24/7 vivid color night vision even in pitch darkness with crystal audio recording."
        ),
        Product(
            id = "prod_6",
            name = "CP Plus 8-Channel 4K Ultra HD Hybrid DVR System",
            category = ProductCategory.CCTV,
            brand = "CP Plus",
            price = 14999,
            originalPrice = 18500,
            rating = 4.8f,
            reviewCount = 31,
            inStock = true,
            isFeatured = false,
            badge = "Commercial Grade",
            specs = listOf("8 Channel 4K Real-time recording", "H.265+ Compression saving 70% storage", "Smart Human & Vehicle Motion Detection", "HDMI & VGA Dual Output", "Supports up to 10TB HDD"),
            description = "Ideal surveillance backbone for commercial retail shops, godowns, schools, and offices in Tezpur."
        ),
        Product(
            id = "prod_7",
            name = "LG UltraGear 27\" QHD (1440p) Nano IPS 180Hz 1ms",
            category = ProductCategory.MONITORS,
            brand = "LG",
            price = 21990,
            originalPrice = 26500,
            rating = 4.9f,
            reviewCount = 27,
            inStock = true,
            isFeatured = true,
            badge = "Pro Esports",
            specs = listOf("27-inch 2560x1440 QHD IPS", "180Hz Refresh Rate (OC)", "1ms GtG Response Time", "HDR400 & 98% DCI-P3 Color", "G-Sync Compatible & FreeSync", "Ergonomic Height/Pivot Stand"),
            description = "The gold standard gaming and color-accurate video editing monitor with razor-sharp response."
        ),
        Product(
            id = "prod_8",
            name = "Kingston KC3000 1TB PCIe 4.0 NVMe SSD",
            category = ProductCategory.PC_PARTS,
            brand = "Kingston",
            price = 8400,
            originalPrice = 9900,
            rating = 4.9f,
            reviewCount = 53,
            inStock = true,
            isFeatured = false,
            badge = "Speed King",
            specs = listOf("7,000 MB/s Read, 6,000 MB/s Write", "Graphene low profile heat spreader", "5-Year Official Warranty", "PS5 & PC Compatible"),
            description = "Instant load times for games and heavy software. Free cloning service included at Cinex store."
        ),
        Product(
            id = "prod_9",
            name = "Redragon K552 RGB Mechanical Gaming Keyboard",
            category = ProductCategory.ACCESSORIES,
            brand = "Redragon",
            price = 2899,
            originalPrice = 3699,
            rating = 4.7f,
            reviewCount = 89,
            inStock = true,
            isFeatured = false,
            badge = "Best Budget",
            specs = listOf("Outemu Blue Tactile Switches", "Solid Metal & ABS Construction", "Full RGB Per-Key Backlighting", "Anti-Ghosting 87 Keys Tenkeyless"),
            description = "Tactile mechanical clickiness designed for fast FPS twitch-reactions and long typing sessions."
        ),
        Product(
            id = "prod_10",
            name = "HP Smart Tank 580 All-in-One WiFi Color Printer",
            category = ProductCategory.ACCESSORIES,
            brand = "HP",
            price = 13999,
            originalPrice = 16500,
            rating = 4.6f,
            reviewCount = 47,
            inStock = true,
            isFeatured = false,
            badge = "Office Essential",
            specs = listOf("Print, Scan, Copy with WiFi Direct", "Extremely Low Cost (10p black, 20p color)", "Up to 6,000 black & 6,000 color pages in box", "1 Year Onsite Warranty"),
            description = "Cost-effective ink tank printing for school projects, legal documents, and daily business invoices."
        )
    )

    val services = listOf(
        ServiceItem(
            id = "serv_1",
            title = "Laptop Chip-Level Motherboard Repair",
            category = ServiceCategory.REPAIR,
            startingPrice = 1200,
            turnaround = "24 - 48 Hours",
            warranty = "90 Days Warranty",
            isPopular = true,
            description = "Specialized micro-soldering for short-circuited motherboards, dead power ICs, liquid damage, and no-display issues without replacing costly logic boards.",
            features = listOf("BGA rework station precision", "Component-level IC replacement", "Short circuit tracing with thermal cam", "Original genuine components only")
        ),
        ServiceItem(
            id = "serv_2",
            title = "Laptop Screen & Broken Hinge Replacement",
            category = ServiceCategory.REPAIR,
            startingPrice = 1800,
            turnaround = "Same Day (2 - 4 Hours)",
            warranty = "6 Months Replacement Warranty",
            isPopular = true,
            description = "Cracked LCD/LED panels, touchscreens, high-refresh 144Hz displays, and structural hinge fabrication to prevent body cracking.",
            features = listOf("Original A+ grade IPS panels", "Zero dead-pixel guarantee", "Reinforced internal brass threading", "All brands: Asus, Dell, HP, Lenovo, Acer, Apple")
        ),
        ServiceItem(
            id = "serv_3",
            title = "Custom PC Assembly & Cable Master Craft",
            category = ServiceCategory.UPGRADES,
            startingPrice = 1500,
            turnaround = "4 - 6 Hours",
            warranty = "Lifetime Cinex Labor Warranty",
            isPopular = true,
            description = "Flawless cable management, optimal intake/exhaust airflow layout, thermal paste spread, BIOS flashing, and XMP/EXPO memory tuning.",
            features = listOf("Thermal benchmarking test included", "ARGB synchronization", "Windows 11 Pro installation & drivers", "Safe doorstep delivery available")
        ),
        ServiceItem(
            id = "serv_4",
            title = "Hikvision CCTV & IP Camera Installation",
            category = ServiceCategory.CCTV,
            startingPrice = 2500,
            turnaround = "1 Day Onsite",
            warranty = "1 Year Free Service Support",
            isPopular = true,
            description = "Professional surveillance installation for residences, retail showrooms, tea estates, schools, and hospitals across Sonitpur district.",
            features = listOf("Free site survey & angle planning", "Concealed weatherproof wiring", "Remote view on phone & tablet", "DVR/NVR backup configuration")
        ),
        ServiceItem(
            id = "serv_5",
            title = "Ultra-Fast SSD & RAM Speed Booster",
            category = ServiceCategory.UPGRADES,
            startingPrice = 850,
            turnaround = "1 - 2 Hours",
            warranty = "3 - 5 Years Brand Warranty",
            isPopular = false,
            description = "Revive your sluggish old laptop or desktop. We clone your existing Windows, apps, and files exactly as-is to a lightning-fast NVMe/SATA SSD.",
            features = listOf("100% zero data loss guarantee", "Boot time reduced from 2 mins to 8 secs", "Genuine Crucial, Kingston, Samsung SSDs", "Free internal dust blower cleaning")
        ),
        ServiceItem(
            id = "serv_6",
            title = "Deep Thermal Repaste & Dust Decontamination",
            category = ServiceCategory.UPGRADES,
            startingPrice = 650,
            turnaround = "1 Hour Express",
            warranty = "Guaranteed Temp Drop",
            isPopular = false,
            description = "Eliminate thermal throttling and gaming frame drops. Complete heatsink wash, fan bearing lubrication, and premium Thermal Grizzly paste application.",
            features = listOf("Thermal Grizzly Kryonaut / Arctic MX-6", "Ultrasonic fan blade clean", "Pre & post FurMark thermal report", "Extends GPU & CPU lifespan")
        ),
        ServiceItem(
            id = "serv_7",
            title = "Data Recovery from Dead HDD, SSD & Flash Drives",
            category = ServiceCategory.REPAIR,
            startingPrice = 1500,
            turnaround = "1 - 3 Days",
            warranty = "Strict Privacy Confidentiality",
            isPopular = false,
            description = "Recovery of priceless photos, documents, and business databases from formatted, corrupted, clicking, or water-damaged storage drives.",
            features = listOf("Clean room head replacement if needed", "Firmware repair", "No data = No charge policy", "Encrypted delivery")
        ),
        ServiceItem(
            id = "serv_8",
            title = "Annual Maintenance Contract (AMC) for Institutes",
            category = ServiceCategory.NETWORKING,
            startingPrice = 8000,
            turnaround = "Quarterly / On-Call",
            warranty = "Priority SLA (2-Hour Response)",
            isPopular = false,
            description = "Total IT infrastructure peace of mind for offices, schools, colleges, banks, and diagnostic clinics in Tezpur.",
            features = listOf("Quarterly preventive maintenance", "Instant on-call engineer visit", "Network cabling & router diagnostics", "Discounted hardware spare parts")
        )
    )

    val pcPartsCatalog = listOf(
        // Processors
        PcPart("cpu_1", PartType.PROCESSOR, "Intel Core i5-14400F (10 Cores, up to 4.7GHz)", "Intel", 15400, 65, 80, "6 P-cores + 4 E-cores, LGA1700", true),
        PcPart("cpu_2", PartType.PROCESSOR, "AMD Ryzen 5 7600 (6 Cores, 12 Threads)", "AMD", 18200, 65, 84, "Zen 4 5.1GHz Boost, AM5 Platform", true),
        PcPart("cpu_3", PartType.PROCESSOR, "AMD Ryzen 7 7800X3D (World's Best Gaming CPU)", "AMD", 38900, 120, 99, "96MB 3D V-Cache, AM5, 5.0GHz Boost", true),
        PcPart("cpu_4", PartType.PROCESSOR, "Intel Core i7-14700K (20 Cores, up to 5.6GHz)", "Intel", 37500, 253, 97, "8 P-cores + 12 E-cores, LGA1700, Unlocked", false),
        PcPart("cpu_5", PartType.PROCESSOR, "AMD Ryzen 5 5600 (Budget King 6C/12T)", "AMD", 11200, 65, 68, "Zen 3, AM4, Great for 1080p gaming", false),

        // Motherboards
        PcPart("mb_1", PartType.MOTHERBOARD, "MSI PRO B760M-A WiFi DDR5", "MSI", 14500, 25, 80, "LGA1700, PCIe 4.0, Dual M.2, WiFi 6E", true),
        PcPart("mb_2", PartType.MOTHERBOARD, "Gigabyte B650M Gaming WiFi AM5", "Gigabyte", 12800, 25, 82, "AM5, DDR5, PCIe 4.0, Realtek 2.5GbE", true),
        PcPart("mb_3", PartType.MOTHERBOARD, "ASUS ROG Strix B650-A Gaming WiFi", "ASUS", 23900, 30, 95, "White aesthetic, Heavy heatsinks, PCIe 5.0 M.2", false),
        PcPart("mb_4", PartType.MOTHERBOARD, "Gigabyte B550M DS3H AC (AM4)", "Gigabyte", 8900, 20, 65, "Budget AM4, DDR4, Dual M.2, WiFi AC", false),

        // GPUs
        PcPart("gpu_1", PartType.GRAPHICS_CARD, "Nvidia GeForce RTX 4060 8GB GDDR6", "ZOTAC", 27900, 115, 75, "DLSS 3 Frame Gen, 1080p Ultra champ", true),
        PcPart("gpu_2", PartType.GRAPHICS_CARD, "Nvidia GeForce RTX 4070 Super 12GB", "ASUS TUF", 58900, 220, 91, "1440p Maxed Ray Tracing, 12GB GDDR6X", true),
        PcPart("gpu_3", PartType.GRAPHICS_CARD, "Nvidia GeForce RTX 4070 Ti Super 16GB", "Gigabyte", 76900, 285, 96, "16GB VRAM, 4K Gaming, AI & Video Editing", true),
        PcPart("gpu_4", PartType.GRAPHICS_CARD, "AMD Radeon RX 7800 XT 16GB", "Sapphire", 49900, 263, 89, "Huge 16GB VRAM, Raw Rasterization Beast", false),
        PcPart("gpu_5", PartType.GRAPHICS_CARD, "Nvidia GeForce RTX 3050 6GB (Budget)", "MSI", 16800, 70, 52, "Low power, no external connector needed", false),

        // RAM
        PcPart("ram_1", PartType.RAM, "Corsair Vengeance 32GB (16x2) DDR5 6000MHz", "Corsair", 9800, 15, 92, "CL30 Intel XMP & AMD EXPO certified", true),
        PcPart("ram_2", PartType.RAM, "Kingston Fury Beast 16GB (8x2) DDR5 5600MHz", "Kingston", 5400, 10, 75, "High stability, plug and play", true),
        PcPart("ram_3", PartType.RAM, "G.Skill Trident Z5 RGB 32GB DDR5 6400MHz", "G.Skill", 12500, 15, 95, "Premium aluminum heatspreader & vivid RGB", false),
        PcPart("ram_4", PartType.RAM, "Crucial 16GB (8x2) DDR4 3200MHz", "Crucial", 3400, 8, 60, "Classic reliable DDR4 memory kit", false),

        // Storage
        PcPart("ssd_1", PartType.STORAGE, "WD Black SN770 1TB NVMe PCIe 4.0 SSD", "Western Digital", 6900, 8, 88, "5150 MB/s speed, 5-Year WD warranty", true),
        PcPart("ssd_2", PartType.STORAGE, "Kingston KC3000 2TB High-End NVMe 7000MB/s", "Kingston", 14900, 10, 98, "Blazing Gen4 speeds, huge 2TB capacity", true),
        PcPart("ssd_3", PartType.STORAGE, "Crucial P3 Plus 1TB PCIe 4.0 M.2 SSD", "Crucial", 5400, 7, 78, "5000 MB/s budget high-speed drive", false),

        // Power Supply
        PcPart("psu_1", PartType.POWER_SUPPLY, "DeepCool PM750D 750W 80+ Gold", "DeepCool", 5900, 0, 85, "Non-modular, Japanese capacitors, 5-Yr warranty", true),
        PcPart("psu_2", PartType.POWER_SUPPLY, "Corsair RM850e 850W 80+ Gold Fully Modular", "Corsair", 10500, 0, 96, "ATX 3.0 & PCIe 5.0 ready with 12VHPWR cable", true),
        PcPart("psu_3", PartType.POWER_SUPPLY, "Ant Esports VS600L 600W Bronze", "Ant Esports", 2600, 0, 60, "Budget 600W for entry esports rigs", false),

        // Cabinet
        PcPart("case_1", PartType.CABINET, "Ant Esports Crystal X1 ARGB Mid-Tower", "Ant Esports", 3999, 10, 75, "Dual Tempered Glass, 4x ARGB PWM fans", true),
        PcPart("case_2", PartType.CABINET, "Lian Li Lancool 216 RGB High Airflow", "Lian Li", 8400, 15, 96, "2x 160mm massive front intake fans, modular", true),
        PcPart("case_3", PartType.CABINET, "DeepCool CC560 V2 Mesh Black", "DeepCool", 4200, 10, 80, "Full mesh front panel, clean cable routing", false),

        // Cooling
        PcPart("cool_1", PartType.COOLER, "DeepCool AG400 ARGB 120mm Air Cooler", "DeepCool", 1900, 5, 75, "4 heatpipes, TDP up to 220W, silent fan", true),
        PcPart("cool_2", PartType.COOLER, "DeepCool LS720 SE 360mm ARGB Liquid AIO", "DeepCool", 8800, 20, 98, "Triple 120mm fans, aluminum radiator, leak-free", true),
        PcPart("cool_3", PartType.COOLER, "Thermalright Peerless Assassin 120 SE Dual Tower", "Thermalright", 3400, 8, 90, "Dual heatsink tower, cools i7/Ryzen 7 easily", false)
    )

    fun addBooking(
        customerName: String,
        phone: String,
        deviceModel: String,
        issueDescription: String,
        estimatedCost: Long = 1000
    ): String {
        val newId = "CX-${(78404..78999).random()}"
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val todayStr = sdf.format(Date())
        val newTicket = RepairTicket(
            ticketId = newId,
            customerName = customerName,
            phone = phone,
            deviceModel = deviceModel,
            issueReported = issueDescription,
            receivedDate = todayStr,
            estimatedDelivery = "2 Days from today",
            status = TicketStatus.RECEIVED,
            estimatedCost = estimatedCost,
            notes = "Ticket created via Cinex Mobile Portal. Bring device to Santa Plaza, S.C. Road counter or contact Gautam da for pickup."
        )
        _tickets.value = listOf(newTicket) + _tickets.value
        return newId
    }

    fun addReview(author: String, location: String, rating: Int, comment: String, serviceTag: String) {
        val newRev = CustomerReview(
            id = "rev_${System.currentTimeMillis()}",
            author = author,
            location = location,
            rating = rating,
            date = "Just now",
            comment = comment,
            verifiedPurchase = true,
            serviceTag = serviceTag
        )
        _reviews.value = listOf(newRev) + _reviews.value
    }
}
