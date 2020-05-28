# CalenderReminderApp
Mobile Programming - Calender Reminder App (Android)
İpek Koç 16011073
Mobil programlama - Dönem Projesi

# APK dosyası ilk sayfadadır

Proje videosu:
https://www.youtube.com/watch?v=0kmPcTPKlY4

Sistem içerisinde etkinlik filtreleme, kaydetme, silme, güncelleme, paylaşma seçenekleri bulunmaktadır. Zil sesi dahili sistemden seçilir. Bildirimlerde alarm ile birlikte titreşim ve notification da yer almaktadır. Etkinlik ekleme ve silme sırasında oluşabilecek kullanıcı hatalarının önüne geçilmiştir. Mümkün olduğunca karşılaşılabilecek hatalar ele alınarak tüm modüller gerçekleştirilmiştir.

# Önemli
Yapılan son kod düzenlemelerinde 1 satır kod yanlış yere taşındığından birden fazla alarm set edilmesinde sorun oluşturma ihtimaline sebep olmuştur. Teslim süresi geçtiği için herhangi bir değişiklik yapılamayacağından buradan belirtmek isterim.

EventActivity.java class'ında 316.satırda yer alan işlemin 318-319. satır arasında yer alması daha sağlıklı bir sistem için gereklidir. Aksi halde birden fazla alarm set etme modülü çalışmakla birlikte yalnızca zamanlama konusunda hataya yol açabilmektedir.
