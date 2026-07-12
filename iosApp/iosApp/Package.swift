// swift-tools-version:5.2

import PackageDescription

let package = Package(
    name: "Habits",
    products: [
            .app(
                name: "Habits",
                targets: ["Habits"]
            ),
        ],
    targets: [
        .target(
            name: "Habits",
            resources: [
                .process("Resources/challenge.html")
            ]
        ),
    ]
)
