var language = navigator.languages && navigator.languages[0] ||
               navigator.language ||
               navigator.userLanguage;

var set_lang = function(locale){
  var lang = locale.substring(0,2);

  if (lang === 'en'){
    $.i18n().locale = 'en-US'; //$(this).data('locale');
  } else if (lang === 'fr') {
    $.i18n().locale = 'fr-FR';
  } else {
    $.i18n().locale = 'en-US'; 
  }
  console.log(lang);
  $('body').i18n();

}

$.i18n().load( {
  'en-US': 'assets/js/lang/en.json',
  'fr-FR': 'assets/js/lang/fr.json'
  }).done(function() {
    set_lang(language);
    $('.switch-locale').on('click', 'a', function(e) {
      e.preventDefault();
      var data = $(this).data('locale');
      set_lang(data);
    });
  });


  // var set_locale_to = function(locale) {
  //   if (locale)
  //     $.i18n().locale = locale;
  //     $('body').i18n();
  // };

  // jQuery(function() {
  //   $.i18n().load( {
  //     'en': 'assets/js/lang/en.json',
  //     'fr': 'assets/js/lang/fr.json'
  //   } ).done(function() {
  //     set_locale_to(url('?locale'));
  //     console.log($.i18n().locale);
  //     History.Adapter.bind(window, 'statechange', function(){
  //       set_locale_to(url('?locale'));
  //     });

  //     $('.switch-locale').on('click', 'a', function(e) {
  //       e.preventDefault();
  //       History.pushState(null, null, "?locale=" + $(this).data('locale'));
  //     });
  //   });
  // });