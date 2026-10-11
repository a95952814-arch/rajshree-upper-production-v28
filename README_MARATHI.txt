Rajshree Shoes Upper Production App — Android Project V17 (V16 Web App integrated)

या ZIP मध्ये सध्याच्या V16 वेब ॲपचा index.html Android WebView मध्ये जोडला आहे. हा सोर्स प्रोजेक्ट आहे; तयार APK नाही.

APK तयार करण्याची सोपी पद्धत (GitHub Actions):
1. ZIP Extract करा.
2. तुमच्या GitHub repository a95952814-arch/rajshree-upper-production मध्ये जुन्या फाईल्सच्या जागी या प्रोजेक्टमधील फाईल्स upload करा. .github/workflows/main.yml ही फाईलसुद्धा upload करा.
3. GitHub मध्ये Actions उघडा.
4. Build Rajshree Shoes APK निवडा → Run workflow → Run workflow.
5. Build पूर्ण झाल्यावर workflow run मध्ये Artifacts खाली rajshree-shoes-apk-v17 डाउनलोड करा.
6. ZIP Extract करून app-debug.apk मोबाईलवर पाठवा आणि install करा.

महत्त्वाचे:
- App फोनमध्ये स्थानिक WebView storage मध्ये डेटा ठेवते. Chrome मधील जुन्या prototype चा डेटा आपोआप Android app मध्ये येत नाही.
- App uninstall केल्यास स्थानिक डेटा हटू शकतो; नियमित backup घ्या.
- WhatsApp रिपोर्ट तयार झाल्यावर Send वापरकर्त्याने दाबायचे आहे.
- या वातावरणात Android SDK उपलब्ध नसल्यामुळे APK compile करून दिलेला नाही; GitHub Actions मधून build करावा.
