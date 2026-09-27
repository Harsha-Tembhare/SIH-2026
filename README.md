# 🛡️ Aegis Health — SIH 2026

> **Smart India Hackathon 2026** — Real-time AI-powered health monitoring for soldiers, disaster responders, and civilians in high-risk environments.

---

## 📱 About

**Aegis Health** is an Android application that provides continuous, offline-capable physiological monitoring using BLE wearable sensors. It features on-device AI risk assessment, encrypted local storage, and an offline-first disaster mode — designed to operate in environments with no internet connectivity.

---

## ✨ Features

| Feature | Description |
|---|---|
| 🫀 **Live Vitals Monitoring** | Real-time ECG, SpO₂, heart rate, temperature, AQI, and motion data via BLE |
| 🤖 **Explainable AI Risk Engine** | On-device risk scoring with natural-language factor explanations |
| 📊 **Adaptive Personal Baseline** | Learns and adapts to each user's unique physiological norms |
| 🚨 **Automated Alerts** | Cardio, respiratory, heat stroke, fall, and pollution anomaly detection |
| 🔴 **Quick SOS / Disaster Mode** | Compact hex emergency payloads via BLE mesh or offline SMS |
| 🔒 **Zero-Cloud Privacy** | SQLCipher-encrypted DB + Android Keystore; all data stays on device |
| 📡 **BLE Simulator** | Scenario-based sensor simulator for testing without hardware |
| 🌐 **Web Preview UI** | React/Vite companion UI for dashboard prototyping |

---

## 🏗️ Architecture

```
aegishealth/
├── data/
│   ├── local/          # Room + SQLCipher encrypted database
│   └── repository/     # BLE hardware manager & sensor simulator
├── domain/
│   ├── ai/             # Adaptive baseline + Explainable AI engine
│   ├── disaster/       # Disaster mode & offline emergency payloads
│   ├── model/          # Data models (vitals, alerts, AI assessments)
│   └── signal/         # Signal processing & waveform generation
└── ui/
    ├── components/     # Reusable Compose UI components
    ├── navigation/     # Bottom-nav & screen routing
    ├── screens/        # Full screens (Dashboard, LiveMonitor, AI Risk, etc.)
    └── theme/          # Material 3 dark theme
```

---

## 🛠️ Tech Stack

- **Language:** Kotlin 2.1.0
- **UI:** Jetpack Compose + Material 3
- **Database:** Room 2.7 + SQLCipher 4.5 (encrypted)
- **Security:** Android Keystore + EncryptedSharedPreferences
- **BLE:** Android Bluetooth LE APIs
- **Build:** Gradle 9 with KSP, configuration cache enabled
- **Min SDK:** Android 8.0 (API 26) · **Target SDK:** Android 15 (API 35)

---

## 🚀 Getting Started

### Prerequisites

- Android Studio Meerkat or newer
- Android SDK 35
- A device or emulator running Android 8.0+

### Clone & Build

```bash
git clone https://github.com/<your-username>/SIH-2026.git
cd SIH-2026
```

Open the project in **Android Studio** and let Gradle sync. Then run on your device or emulator.

> **Note:** `local.properties` is excluded from version control. Android Studio will generate it automatically with your local SDK path.

### Web Preview (Optional)

```bash
cd web_preview
npm install
npm run dev
```

---

## 📸 Screens

| Dashboard | Live Monitor | AI Risk | Disaster Mode |
|---|---|---|---|
| Vital ring cards with real-time values | ECG waveform + SpO₂ timeline | Risk gauge + explainable factors | SOS payload + offline SMS queue |

---

## 🔐 Privacy & Security

- **Zero-cloud by default** — no data leaves the device
- All health records encrypted with SQLCipher + Android Keystore
- Auto-purge vault on tamper detection
- Per-sensor consent toggles in Privacy settings

---

## 🤝 Contributing

This project was built for **Smart India Hackathon 2026**. Contributions, suggestions, and bug reports are welcome — open an issue or submit a pull request.

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).

---

*Built with ❤️ for SIH 2026 — Team #SIH26181*
