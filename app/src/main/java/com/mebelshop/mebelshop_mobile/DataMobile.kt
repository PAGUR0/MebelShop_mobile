package com.mebelshop.mebelshop_mobile

class DataMobile {
    var listCategoryProduct: List<CategoryProduct>? = null
    var listProduct: List<Product>? = null
    var listShop: List<Shop>? = null

    init {
        listCategoryProduct = listOf(
            CategoryProduct("Школьная мебель", "image/chair_1_1.png"),
            CategoryProduct("Мебель для маломобильных граждан", "image/table_1_1.png"),
            CategoryProduct("Столы", "image/table_1_1.png"),
            CategoryProduct("Стулья", "image/chair_2_1.png"),
            CategoryProduct("Сантехника", "image/toilet_1.png"),
            CategoryProduct("Люстры", "image/Ceiling_lamp_003_1.png")
        )

        listShop = listOf(
            Shop("Школьная мебель", "image/Omels.jpg", "Производим удобную и качесвтенную для школьников", "ООО \"ОМЕЛА\"", 102342456)
        )

        listProduct = listOf(
            Product(
                "Стол для инвалидов-колясочников",
                7200,
                null,
                listShop!![0],
                listOf(listCategoryProduct!![0], listCategoryProduct!![1], listCategoryProduct!![2]),
                images = listOf("image/table_1_1.png", "image/table_1_2.png"),
                description = "Отталкиваясь от размеров инвалидных колясок для учеников и предписаниям, стол для инвалидов колясочников обязан быть регулируемым по высоте, удерживать высокую вертикальную нагрузку, располагать свободным пространством перед ногами сидящего",
                attributes = mapOf(
                    "Размер:" to "1000х800х950",
                    "Тип:" to "Регулируемый, с вырезом",
                    "Высота стола:" to "600-950",
                    "Размер столешницы:" to "1000Х600",
                    "Цвет:" to "Серый, Светлое дерево",
                    "Материалы:" to "Сталь, Светлое дерево, Полимерное защитное покрытие, Полимерно-порошковая краска"
                ),
                model = "models/table_1.glb"
            ),

            Product(
                "Парта",
                5000,
                null,
                listShop!![0],
                listOf(listCategoryProduct!![0], listCategoryProduct!![2]),
                images = listOf("image/Table_001_4.png", "image/Table_001_2.png", "image/Table_001_3.png", "image/Table_001.png"),
                description = "Отталкиваясь от размеров инвалидных колясок для учеников и предписаниям, стол для инвалидов колясочников обязан быть регулируемым по высоте, удерживать высокую вертикальную нагрузку, располагать свободным пространством перед ногами сидящего",
                attributes = mapOf(
                    "Размер:" to "400х800х450",
                    "Тип:" to "Нерегулируемый",
                    "Высота стола:" to "450",
                    "Размер столешницы:" to "800Х400",
                    "Цвет:" to "Черный, Светлое дерево",
                    "Материалы:" to "Сталь, Светлое дерево, Полимерное защитное покрытие, Полимерно-порошковая краска"
                ),
                model = "models/Table.glb"
            ),

            Product(
                "Стул школьный",
                4000,
                5000,
                listShop!![0],
                listOf(listCategoryProduct!![0], listCategoryProduct!![3]),
                images = listOf("image/chair_1_1.png", "image/chair_1_2.png"),
                description = "Сиденье и спинка изготовлены из гнутоклееной фанеры толщиной не менее 8 мм, покрыты бесцветным лаком. Металлический каркас выполнен из профиля квадратного сечения 25×25 мм и 20×20 мм с толщиной стенок не менее 1.2 мм и покрыт порошковой краской, стойкой к химическим и механическим воздействиям. На свободных концах труб установлены заглушки из ударопрочных полимеров. Каркас стула имеет полимерные подпятники, предотвращающие повреждение напольного покрытия. Сиденье и спинка крепятся к основанию при помощи мебельных болтов и гаек. Регулировка высоты осуществляется с помощью винтовых соединений с гайкой Эриксона. Вся крепежная фурнитура на металлокаркасах скрыта внутри трубы для продления срока службы изделия. Стул имеет цветовую маркировку в соответствии с ростовыми группами.",
                attributes = mapOf(
                    "Размер:" to "650х700х1000",
                    "Тип:" to "Нерегулируемый",
                    "Количество ножек:" to "4",
                    "Макс нагрузка:" to  "120 кг",
                    "Цвет:" to "Серый, Светлое дерево",
                    "Материалы:" to "Сталь, Светлое дерево, Полимерное защитное покрытие, Полимерно-порошковая краска"
                ),
                model = "models/school_chair_1.glb"
            ),

            Product(
                "Офисный стул",
                8709,
                null,
                listShop!![0],
                listOf(listCategoryProduct!![3]),
                images = listOf("image/chair_2_1.png", "image/chair_2_2.png"),
                description = "Компьютерное кресло руководителя Бюрократ CH-868N представляет собой удобное и практичное кресло, которое позволяет выдерживать нагрузку до 120 кг. Кресло станет отличным решением как для домашнего использования, так и для офиса.",
                attributes = mapOf(
                    "Размер:" to "665х665х875",
                    "Тип:" to "Регулируемый",
                    "Количество ножек:" to "4",
                    "Макс нагрузка:" to  "120 кг",
                    "Цвет:" to "Черный, Коричневый",
                    "Материалы:" to "Пластик, Фанера, Ткань, Поролон"
                ),
                model = "models/office_chair_1.glb"
            ),

            Product(
                "Бумажный абажур",
                1209,
                null,
                listShop!![0],
                listOf(listCategoryProduct!![5]),
                images = listOf("image/Ceiling_lamp_001_1.png","image/Ceiling_lamp_001_2.png","image/Ceiling_lamp_001_3.png","image/Ceiling_lamp_004_1.png", ),
                description = "Абажур из бумаги",
                attributes = mapOf(
                    "Размер:" to "600х600х1000",
                    "Цвет:" to "Белый",
                    "Материалы:" to "Бумага, Пластик"
                ),
                model = "models/Ceiling_lamp_001.glb"
            ),

            Product(
                "Люстра с 3 плафонами",
                6500,
                8000,
                listShop!![0],
                listOf(listCategoryProduct!![5]),
                images = listOf("image/Ceiling_lamp_003_1.png","image/Ceiling_lamp_003_2.png","image/Ceiling_lamp_003_3.png","image/Ceiling_lamp_003_4.png", ),
                description = "Люстра с 3 плафонами",
                attributes = mapOf(
                    "Размер:" to "200х1000х1000",
                    "Цвет:" to "Коричневый, Черный",
                    "Материалы:" to "Пластик"
                ),
                model = "models/Ceiling_lamp_003.glb"
            ),

            Product(
                "Круглая люстра",
                5500,
                null,
                listShop!![0],
                listOf(listCategoryProduct!![5]),
                images = listOf("image/Ceiling_lamp_002_1.png","image/Ceiling_lamp_002_2.png","image/Ceiling_lamp_002_3.png","image/Ceiling_lamp_002_4.png", ),
                description = "Круглая люстра",
                attributes = mapOf(
                    "Размер:" to "400х400х1000",
                    "Цвет:" to "Белый",
                    "Материалы:" to "Стекло, Сталь"
                ),
                model = "models/Ceiling_lamp_002.glb"
            ),

            Product(
                "Люстра \"Дождь\"",
                5500,
                null,
                listShop!![0],
                listOf(listCategoryProduct!![5]),
                images = listOf("image/Ceiling_lamp_004_1.png","image/Ceiling_lamp_004_2.png","image/Ceiling_lamp_004_3.png","image/Ceiling_lamp_004_4.png", ),
                description = "Люстра \"Дождь\"",
                attributes = mapOf(
                    "Размер:" to "270х270х1000",
                    "Цвет:" to "Золотой",
                    "Материалы:" to "Пластик, Стекло, Сталь"
                ),
                model = "models/Ceiling_lamp_004.glb"
            ),

            Product(
                "Чаша Генуя",
                32907,
                null,
                listShop!![0],
                listOf(listCategoryProduct!![4]),
                images = listOf("image/toilet_1.png", ),
                description = "Чаша Генуя",
                attributes = mapOf(
                    "Размер:" to "400х600х300",
                    "Цвет:" to "Белый",
                    "Материалы:" to "Керамика"
                ),
                model = "models/toilet_1.glb"
            )
            )
    }
}