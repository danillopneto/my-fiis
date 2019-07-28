function () {
    window.locations = [];

    window.loadLocations = function(keywords) {
        locations = [];

        $('#fiis-map-index #loading-map').removeClass('hidden');
        $('#fiis-map-index #map').addClass('hidden');

        for (var i = 0; i < keywords.length; i++) {
            $.ajax({
              method: 'post',
              url: '/mapa-fiis/filter',
              data: {
                filter: {
                  keyword: keywords[i].trim()
                },
              },
              beforeSend: function(xhr) {
                xhr.setRequestHeader('X-CSRF-Token', $('meta[name="csrf-token"]').attr('content'));
              },
              complete: function(res) {
                var result = res.responseJSON;
                for (var m = 0; m < result.markers.length; m++) {
                    locations.push(result.markers[m]);
                }

                if (keywords.length == i) {
                    loadMapWithMarkers(locations);
                }
              }
            });
        }
    };

    window.loadMapWithMarkers = function (locations) {

        var handler = Gmaps.build('Google');

        handler.buildMap({ internal: {id: 'map'}}, function(){
          var markers = handler.addMarkers(locations);
          handler.bounds.extendWith(markers);
          handler.fitMapToBounds();
          if (locations.length == 1) {
            handler.getMap().setZoom(16);
          }
        });

        $('#fiis-map-index #loading-map').addClass('hidden');
        $('#fiis-map-index #map').removeClass('hidden');
        $('#fiis-map-index #filters-results-count b').html(locations.length);
    };

    $('[id="filter-results-btn"]').off();
    $('[id="filter-results-btn"]').on('click', function() {
        var keywords = $('[name="fii"]').val().split(', ');
        loadLocations(keywords);
    });

    window.startMyFiis = function(keywords) {
        window.myFiis = keywords;

        var btnMyFiis = '<div style="text-align: center;"><button id="btnMyFiis" class="fe-input" style="margin-top: 10px;">Meus FIIs</button></div>';
        $('[id="filter-results-btn"]').after(btnMyFiis);
        $('#btnMyFiis').off();
        $('#btnMyFiis').on('click', function() {
            loadLocations(window.myFiis);
        });
    };
}