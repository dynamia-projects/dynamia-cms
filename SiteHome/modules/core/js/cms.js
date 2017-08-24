(function ($) {
    $.fn.fillSelect = function (options) {
        var settings = $.extend({
            datas: null,
            complete: null,
        }, options);

        this.each(function () {
            var datas = settings.datas;
            if (datas != null) {
                $(this).empty();
                $(this).find('option').remove();
                $(this).append('<option disabled selected>---</option>');
                for (var key in datas) {
                    $(this).append('<option value="' + datas[key].id + '"+>' + datas[key].name + '</option>');
                }
            }
            if ($.isFunction(settings.complete)) {
                settings.complete.call(this);
            }
        });

    }

}(jQuery));


function loadCountries(select, callback) {
    var URL = "/api/locations/countries";
    $.getJSON(URL, function (data) {
        console.log("Loading countries: " + JSON.stringify(data));
        $(select).fillSelect({datas: data});
        if (callback) {
            callback();
        }
    });
}

function loadRegions(selectCountry, selectRegions, callback) {
    var country = $(selectCountry).find("option:selected").text();
    if (country) {

        var url = "/api/locations/countries/" + country + "/regions";
        console.log("Loading regions from " + country);

        $.getJSON(url, function (data) {
            $(selectRegions).fillSelect({datas: data});

            $(selectRegions).removeAttr("disabled");
            if (callback) {
                callback();
            }
        });
    }
}

function loadCities(selectCountry, selectRegions, selectCities, callback) {
    var country = $(selectCountry).find("option:selected").text();
    var region = $(selectRegions).find("option:selected").val();

    var url = "/api/locations/countries/" + country + "/regions/" + region + "/cities";
    console.log("Loading cities from " + country + " - " + region);

    $.getJSON(url, function (data) {
        $(selectCities).fillSelect({datas: data});
        $(selectCities).removeAttr("disabled");
        if (callback) {
            callback();
        }
    });
}

function initCitySelector(selectCountry, selectRegions, selectCities, callback) {

    loadCountries(selectCountry, callback);
    $(selectRegions).attr("disabled", "disabled");
    $(selectCities).attr("disabled", "disabled");

    $(selectCountry).change(function () {
        loadRegions(selectCountry, selectRegions);

    });

    $(selectRegions).change(function () {
        loadCities(selectCountry, selectRegions, selectCities);

    });


}

function setSelected(select, value, callback) {
    $(select).val(value);

    if (callback) {
        callback();
    }
}

function isSelectEmpty(select) {
    return $(select).has("option").length == 0;
}

function setSelectedCountry(selectCountry, value) {
    setSelected(selectCountry, value);
}

function setSelectedRegion(selectCountry, selectRegions, value, callback) {
    if (isSelectEmpty(selectRegions)) {
        loadRegions(selectCountry, selectRegions, function () {
            setSelected(selectRegions, value, callback);
        });
    } else {
        setSelected(selectRegions, value, callback);
    }
}

function setSelectedCity(selectCountry, selectRegions, selectCities, value) {
    if (isSelectEmpty(selectCities)) {
        loadCities(selectCountry, selectRegions, selectCities, function () {
            setSelected(selectCities, value);
        });
    } else {
        setSelected(selectCities, value);
    }
}


