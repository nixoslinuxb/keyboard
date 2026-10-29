# Offline Keyboard

[English](README.md) · **Türkçe**

Kotlin ve Jetpack Compose ile yazılmış, tamamen çevrimdışı çalışan açık kaynak bir Android klavyesi (IME). Ağ izni yok, telemetri yok, hesap yok — yazdıklarınız cihazdan çıkmıyor.

![Platform](https://img.shields.io/badge/platform-Android-3DDC84)
![Kotlin](https://img.shields.io/badge/Kotlin-2.2-7F52FF)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4)
![License](https://img.shields.io/badge/license-MIT-blue)

## Özellikler

- **Türkçe Q dizilimi** — ana dizilimde `ğ ü ş ı ö ç`, `tr_TR` alt türü.
- **Aday çubuğu** — bellek içi trie üzerinde çalışan, frekansa göre sıralanmış yerleşik sözlükten (İngilizce + Türkçe) önek önerileri.
- **Sayı satırı** — isteğe bağlı üst rakam satırı.
- **Sembol ve emoji klavyeleri** — ayrı sembol sayfası ve kategorilere ayrılmış sekmeli emoji seçici.
- **Otomatik sayısal PIN klavyesi** — sayısal ve parola alanlarında (Türkçe `şifre`, `parola`, `pin`, `kod` ipuçları dahil) büyük tuşlu sayısal takım otomatik açılır.
- **5 tema** — System, Light, Dark, Gboard Blue (varsayılan), Pitch Black.
- **Titreşim ve tuş sesi**, ikisi de açılıp kapatılabilir; ayrıca sayı satırı ve öneri anahtarları.
- **Ayar uygulaması** — klavyeyi hemen denemek için uygulama içi test alanı, tema seçici ve davranış anahtarları içeren Compose ekranı.

## İndirme

Derlenmiş APK'lar her [sürüme](https://github.com/nixoslinuxb/keyboard/releases) eklenir. `v*` etiketi göndermek (veya Actions sekmesinden **Build APK** iş akışını elle çalıştırmak) bir APK derleyip yayınlar.

> Yayınlanan APK **debug anahtarıyla imzalanmıştır**: yan yükleme (sideload) izinli her cihaza kurulabilir, ancak kendi release anahtarınızla imzalanmış bir uygulamanın üzerine güncelleme olarak kurulamaz.

## Gereksinimler

| Araç | Sürüm |
| --- | --- |
| Android SDK | API 37 (`compileSdk` / `targetSdk`), `minSdk` 36 |
| JDK | 25 (Gradle toolchain tarafından otomatik indirilir) |
| Gradle | 9.7.1 (wrapper dahil) |
| AGP | 9.4.1 |

## Derleme

1. `local.properties` oluşturarak Gradle'a Android SDK yolunu gösterin:

   ```properties
   sdk.dir=/Android/Sdk/yolu
   ```

2. Debug APK'sını derleyin:

   ```bash
   ./gradlew assembleDebug
   ```

   APK `app/build/outputs/apk/debug/app-debug.apk` konumuna çıkar.

3. Cihaza veya emülatöre kurun:

   ```bash
   ./gradlew installDebug
   # veya
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```

## Klavyeyi etkinleştirme

1. **Ayarlar → Sistem → Diller ve giriş → Ekran klavyesi → Klavyeleri yönet** yolunu izleyin.
2. **Offline Modern Keyboard** seçeneğini açın.
3. Etkin giriş yöntemi olarak seçin (navigasyon çubuğundaki klavye değiştirme düğmesi veya sistem IME seçicisi).
4. Herhangi bir metin alanını açıp yazmaya başlayın — veya **Offline Keyboard** uygulamasını açıp ekranın üstündeki test alanını kullanın.

## Proje yapısı

```
app/src/main/java/com/keyboardd/
├── MainActivity.kt              # Ayar ve yapılandırma ekranı
├── data/
│   ├── DictionaryRepository.kt  # Trie + frekans sıralı sözcük listesi
│   └── KeyboardSettings.kt      # SharedPreferences tabanlı ayarlar
├── service/
│   └── KeyboardService.kt       # Compose arayüzünü barındıran InputMethodService
└── ui/
    ├── KeyboardScreen.kt        # Mod yönlendirme, giriş yönetimi
    ├── components/              # QWERTY, semboller, emoji, PIN, aday çubuğu
    └── theme/                   # Temalar ve renk paletleri
```

## Gizlilik

Uygulama yalnızca tek bir izin bildirir: `VIBRATE`. `INTERNET` izni yoktur; dolayısıyla tuş vuruşları, öneriler veya kullanım verileri cihazdan çıkamaz. Ayarlar yerel olarak `SharedPreferences` içinde tutulur, sözlük ise APK'ya gömülüdür.

## Lisans

[MIT](LICENSE)
