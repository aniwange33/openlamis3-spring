            (function (global, factory) {
	typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
	typeof define === 'function' && define.amd ? define(['exports'], factory) :
	(global = global || self, factory((global.fingerprint = global.fingerprint || {}, global.fingerprint.js = {})));
                }(this, function (exports) { 'use strict';

	var FingerprintService = /** @class */ (function () {
	    function FingerprintService() {
	        this.url = "http://" + window.location.host + "/resources/api/fingerprint";
	    }
	    FingerprintService.prototype.getReaders = function () {
	        var readers = [];
	        return $.ajax({
	            url: this.url + "/readers"
	        }).then(function (res) {
	            res.forEach(function (data) { return readers.push({ id: data.id, name: data.name }); });
	            return readers;
	        });
	    };
	    FingerprintService.prototype.enrolled = function (id) {
	        return $.ajax({
	            url: encodeURI(this.url + "/enrolled-fingers/" + id)
	        });
	    };
	    FingerprintService.prototype.identify = function (reader) {
	        return $.ajax({
	            url: encodeURI(this.url + "/identify/" + encodeURIComponent(reader))
	        });
	    };
	    FingerprintService.prototype.verify = function (reader, id) {
	        return $.ajax({
	            url: encodeURI(this.url + "/verify/" + encodeURIComponent(reader) + "/" + id)
	        });
	    };
	    FingerprintService.prototype.enrol = function (reader, id, finger) {
	        return $.ajax({
	            url: encodeURI(this.url + "/enrol/" + encodeURIComponent(reader) + "/" + id + "/" + finger)
	        });
	    };
	    FingerprintService.prototype.findDuplicate = function (hospNum) {
	        return $.ajax({
	            url: encodeURI(this.url + "/find-duplicate/" + encodeURIComponent(hospNum))
	        });
	    };
	    FingerprintService.prototype.saveDuplicate = function (patientId1, patientId2) {
	        return $.ajax({
	            url: encodeURI(this.url + "/save-duplicate/" + patientId1 + "/" + patientId2)
	        });
	    };
	    return FingerprintService;
	}());

	var FingerprintComponent = /** @class */ (function () {
	    function FingerprintComponent() {
	        this.service = new FingerprintService();
	        this.enrolOperation = false;
	        this.fingers = [];
	        this.body = $('body');
	    }
	    FingerprintComponent.prototype.recordDuplicate = function (patientId) {
	        var _this = this;
	        this.attachStyle();
	        this.buildDuplicateDialog();
	        this.disableButton('.ZebraDialog_Button_0');
	        $('#details').hide();
	        $('#no-details').hide();
	        $('#result-true').hide();
	        $('#result-false').hide();
	        var hospNumInput = $('#hospNum');
	        var patientId2 = 0;
	        hospNumInput.bind('blur', function () {
	            $('#details').hide();
	            $('#no-details').hide();
	            var hospNum = hospNumInput.val().toString();
	            _this.service.findDuplicate(hospNum).then(function (res) {
					console.log('Res', res);
	                if ($('#details tbody tr').length > 2) {
	                    for (var i = 0; i < 4; i++) {
	                        $('#details tbody tr:first-child').remove();
	                    }
	                }
	                $('#result-true').hide();
	                $('#result-false').hide();
	                if (!!res) {
	                    _this.enableButton('.ZebraDialog_Button_0');
	                    patientId2 = res.patientId;
	                    $('#details')
	                        .prepend("\n\t\t\t\t\t\t<tr><td>Gender: </td><td>" + res.gender + "</td></tr>\n\t\t\t\t\t\t")
	                        .prepend("\n\t\t\t\t\t\t<tr><td>Address: </td><td>" + res.address + "</td></tr>\n\t\t\t\t\t\t")
	                        .prepend("\n\t\t\t\t\t\t<tr><td>Phone: </td><td>" + res.phone + "</td></tr>\n\t\t\t\t\t\t")
	                        .prepend("\n\t\t\t\t\t\t<tr><td>Name: </td><td>" + res.name + "</td></tr>\n\t\t\t\t\t\t")
	                        .show();
	                }
	                else {
	                    $('#no-details').show();
	                }
	            });
	        });
	        $('.ZebraDialog_Button_0').bind('click', function () {
	            _this.service.saveDuplicate(patientId, patientId2).then(function (res) {
	                _this.disableButton('.ZebraDialog_Button_0');
	                if (res) {
	                    $('#result-true').show();
	                }
	                else {
	                    $('#result-false').show();
	                }
	            });
	        });
	    };
	    FingerprintComponent.prototype.enrolled = function (id, fnc) {
	        this.service.enrolled(id).then(function (res) {
	            if (res.length > 0) {
	                fnc(true);
	            }
	            else {
	                fnc(false);
	            }
	        });
	    };
	    FingerprintComponent.prototype.enrolledFingers = function (id) {
	        return this.service.enrolled(id);
	    };
	    FingerprintComponent.prototype.enrol = function (id, fnc) {
	        var _this = this;
	        this.enrolOperation = true;
	        this.buildDialog();
	        $('.ZebraDialog_Title').text('Enrol Patient');
	        var form = $('.fingers').append("\n\t  <td colspan=\"3\">\n\t  \t<p style=\"font-weight: bold; font-size: .8em\">The following fingers have been enrolled for patient:</p>\n\t    <ul id=\"fingerList\" style=\" font-size: .9em\">\n\t   ");
	        this.updateEnrolledFingers(id);
	        form.append("\n\t    </ul>\n\t  </td>\n\t  ");
	        $('.fingers1').append("\n\t    <td>\n\t  \t\t<label for=\"fingers\" style=\"font-weight: bold\">Finger:</label>\n\t  \t</td>\n\t  \t<td colspan=\"2\">\n\t  \t\t<select id=\"fingers\" class=\"form-control form-control-sm\" style=\"width: 100%\">\n\t\t\t\t<option selected value=\"\">Choose..</option>\n\t\t\t\t<option value=\"LEFT_THUMB\">Left Thumb</option>\n\t\t\t\t<option value=\"LEFT_INDEX_FINGER\">Left Index Finger</option>\n\t\t\t\t<option value=\"RIGHT_THUMB\">Right Thumb</option>\n\t\t\t\t<option value=\"RIGHT_INDEX_FINGER\">Right Index Finger</option>\n\t  \t\t</select>\n\t  \t</td>\n\t\t");
	        var fingerprintUpdate = $('.fingers2');
	        fingerprintUpdate.append("\n\t\t\t<td>Update fingerprint</td>\n\t\t\t<td><input id=\"update\" type=\"checkbox\"></td>\n\t\t\t<td></td>\n\t\t");
	        fingerprintUpdate.hide();
	        var update = $('#update');
	        $('#fingers, #readers').change(function (evt) {
	            var value = $('#fingers').val();
	            var readers = $('#readers').val();
	            if (!!value && !!readers) {
	                _this.enableButton('.ZebraDialog_Button_1');
	            }
	            else {
	                _this.disableButton('.ZebraDialog_Button_1');
	            }
	            if (_this.fingers.includes(_this.fingerValueToName(value.toString()))) {
	                fingerprintUpdate.show();
	                update.change(function () {
	                    if (!!update.prop('checked')) {
	                        _this.enableButton('.ZebraDialog_Button_1');
	                    }
	                    else {
	                        _this.disableButton('.ZebraDialog_Button_1');
	                    }
	                });
	                update.prop('checked', false);
	                _this.disableButton('.ZebraDialog_Button_1');
	            }
	            else {
	                update.unbind('change');
	                fingerprintUpdate.hide();
	                update.prop('checked', true);
	                if (!!value && !!readers) {
	                    _this.enableButton('.ZebraDialog_Button_1');
	                }
	            }
	        });
	        $('.ZebraDialog_Button_1').bind('click', function (evt) {
	            $('#message').text('Please put a finger on the scanner...');
	            var reader = $('#readers').val().toString();
	            var finger = $('#fingers').val().toString();
	            _this.disableButton('.ZebraDialog_Button_1');
	            _this.service.enrol(reader, id, finger).then(function (res) {
	                if (!!res && !!res.name) {
	                    finger = _this.fingerValueToName(finger);
	                    $('#message').text("Finger " + finger + " for patient " + res.name + " enrolled");
	                    _this.updateEnrolledFingers(id);
	                    fnc(res);
	                }
	                else {
	                    $('#message').text('Could not enrol finger');
	                    fnc(false);
	                }
	                _this.enableButton('.ZebraDialog_Button_1');
	            });
	        }).show();
	        this.disableButton('.ZebraDialog_Button_1');
	    };
	    FingerprintComponent.prototype.verify = function (patientId, fnc) {
	        var _this = this;
	        this.buildDialog();
	        $('.ZebraDialog_Title').text('Verify Patient');
	        $('.ZebraDialog_Button_2').bind('click', function (evt) {
	            $('#message').text('Please put a finger on the scanner...');
	            var reader = $('#readers').val().toString();
	            _this.disableButton('.ZebraDialog_Button_2');
	            return _this.service.verify(reader, patientId).then(function (res) {
	                if (!!res && !!res.name) {
	                    $('#message').text("Patient, " + res.name + " was verified");
	                    fnc(true);
	                }
	                else {
	                    $('#message').text('Could not verify patient');
	                    fnc(false);
	                }
	                _this.disableButton('.ZebraDialog_Button_2');
	            });
	        }).show();
	    };
	    FingerprintComponent.prototype.identify = function (fnc) {
	        var _this = this;
	        this.buildDialog();
	        $('.ZebraDialog_Title').text('Identify Patient');
	        $('.ZebraDialog_Button_0').bind('click', function (evt) {
	            $('#message').text('Please put a finger on the scanner...');
	            var reader = $('#readers').val().toString();
	            _this.disableButton('.ZebraDialog_Button_0');
	            _this.service.identify(reader).then(function (res) {
	                if (!!res && !!res.name) {
	                    $('#message').text("Patient, " + res.name + " was identified");
	                    fnc(res);
	                }
	                else {
	                    $('#message').text('Could not identify patient');
	                    fnc(false);
	                }
	                _this.enableButton('.ZebraDialog_Button_0');
	            });
	        }).show();
	    };
	    FingerprintComponent.prototype.updateEnrolledFingers = function (id) {
	        var _this = this;
	        var fingers = $('#fingerList');
	        $('#fingerList li').remove();
	        this.enrolledFingers(id).then(function (res) {
	            res.forEach(function (finger) {
	                if (!_this.fingers.includes(finger)) {
	                    _this.fingers.push(finger);
	                }
	            });
	            _this.fingers.forEach(function (f) {
	                fingers.append("<li>" + f + "</li>");
	            });
	        });
	    };
	    FingerprintComponent.prototype.updateScanners = function () {
	        this.service.getReaders().then(function (res) {
	            res.forEach(function (reader) {
	                $('#readers').append($("<option></option>").attr("value", reader.name).text(reader.name));
	            });
	        });
	    };
	    FingerprintComponent.prototype.buildDuplicateDialog = function () {
	        var duplicate = $('#duplicate');
	        if (!duplicate.length) {
	            this.body.append("\n\t\t<div id=\"duplicate\">\n\t\t\t<table>\n\t\t\t  <tr>\n\t\t\t    <td>Hospital Number:</td><td><input id=\"hospNum\"></td>\n    \t\t  </tr>\n    \t\t  <tr>\n    \t\t  \t<td colspan=\"2\">Duplicate details:</td>\n    \t\t  </tr>\n    \t\t  <tr id=\"no-details\">\n    \t\t  \t<td colspan=\"2\" style=\"color: red; font-size: 0.8em\">Hospital Number not available in this facility</td>\n\t\t\t  </tr>\n\t\t\t</table>\n\t\t\t<table id=\"details\">\n\t\t\t  <tr id=\"result-false\">\n    \t\t  \t<td colspan=\"2\" style=\"color: red; font-size: 0.8em\">Could not save record; try again later</td>\n\t\t\t  </tr>\n\t\t\t  <tr id=\"result-true\">\n    \t\t  \t<td colspan=\"2\" style=\"color: green; font-size: 0.8em\">Record save!</td>\n\t\t\t  </tr>\n\t\t\t</table>\n</table>\n\t\t</div>\n\t\t");
	        }
	        duplicate = $('#duplicate');
	        new $.Zebra_Dialog({
	            source: { inline: duplicate },
	            title: 'Patient Duplicate Record',
	            overlay_close: false,
	            buttons: [
	                {
	                    caption: 'Close'
	                },
	                {
	                    caption: 'Save Duplicate',
	                    callback: function () {
	                        return false;
	                    }
	                }
	            ]
	        });
	        $('.ZebraDialog').removeClass('ZebraDialog_Icon');
	        $('.ZebraDialog_Button_0').replaceWith("<button class='ZebraDialog_Button_0'>Save Duplicate</button>");
	        $('.ZebraDialog_Button_1').css("background-image", "linear-gradient(to bottom,#b3c9d4,#55575d)");
	    };
	    FingerprintComponent.prototype.buildDialog = function () {
	        var _this = this;
	        this.attachStyle();
	        var fingerprint_dialog = $('#fingerprint_dialog');
	        if (!fingerprint_dialog.length) {
	            this.body.append("\n      <div id=\"fingerprint_dialog\">\n        <div>\n          <table>\n          \t<tr>\n          \t<td style='width: 33% !important;'></td><td style='width: 33% !important;'></td><td style='width: 33% !important;'></td>\n\t\t\t</tr>\n            <tr>\n  \t\t      <td style=\"font-weight: bold;\">Readers:</label>\n  \t\t      <td colspan=\"2\">\n  \t\t        <select id=\"readers\" class=\"form-control form-control-sm\" style=\"width: 100%\">\n    \t\t      <option selected value=\"\">Choose...</option>\n  \t\t        </select>\n  \t\t      </td>  \n  \t\t    </tr>\n  \t\t    <tr class=\"fingers\">\n  \t\t    </tr>\n  \t\t    <tr class=\"fingers1\">\n\t\t\t</tr>\n\t\t\t<tr class=\"fingers2\">\n\t\t\t</tr>\n\t\t  </table>\n\t\t</div>\n\t\t<table>\n\t\t  <tr>\n\t\t    <td>\n      \t      <p id=\"message\" style=\"color: red\"></p>\n      \t    </td>  \n      \t  </tr>\n\t    </table>\n      </div>\n        ");
	        }
	        this.updateScanners();
	        fingerprint_dialog = $('#fingerprint_dialog');
	        var dialog = new $.Zebra_Dialog({
	            source: { inline: fingerprint_dialog },
	            title: 'Fingerprint',
	            overlay_close: false,
	            buttons: [
	                {
	                    caption: 'Close',
	                    callback: function () {
	                        return false;
	                    }
	                },
	                {
	                    caption: 'Start identification',
	                    callback: function () {
	                        return false;
	                    }
	                },
	                {
	                    caption: 'Start enrollment',
	                    callback: function () {
	                        return false;
	                    }
	                },
	                {
	                    caption: 'Start verification',
	                    callback: function () {
	                        return false;
	                    }
	                }
	            ]
	        });
	        $('.ZebraDialog').removeClass('ZebraDialog_Icon');
	        $('.ZebraDialog_Button_0').replaceWith("<button class='ZebraDialog_Button_0'>Start Identification</button>");
	        $('.ZebraDialog_Button_1').replaceWith("<button class='ZebraDialog_Button_1'>Start Enrollment</button>");
	        $('.ZebraDialog_Button_2').replaceWith("<button class='ZebraDialog_Button_2'>Start Verification</button>");
	        $('#readers').change(function (evt) {
	            var value = $('#readers').val();
	            if (!!value) {
	                _this.enableButton('.ZebraDialog_Button_0');
	                _this.enableButton('.ZebraDialog_Button_1');
	                _this.enableButton('.ZebraDialog_Button_2');
	            }
	            else {
	                _this.disableButton('.ZebraDialog_Button_0');
	                _this.disableButton('.ZebraDialog_Button_1');
	                _this.disableButton('.ZebraDialog_Button_2');
	            }
	        });
	        this.disableButton('.ZebraDialog_Button_0').hide();
	        this.disableButton('.ZebraDialog_Button_1').hide();
	        this.disableButton('.ZebraDialog_Button_2').hide();
	        $('.ZebraDialog_Button_3').css("background-image", "linear-gradient(to bottom,#b3c9d4,#55575d)")
	            .click(function () {
	            if (_this.fingers.length === 4) {
					dialog.close();
				}	            
	            else {
	                if (!_this.enrolOperation) {
						dialog.close();
					}
	                var remainder = 4 - _this.fingers.length;
	                var fingers = 'fingers';
	                if (remainder === 1) {
	                    fingers = 'finger';
	                }
	                $('#message').text(remainder + " more " + fingers + " required to fully enroll patient");
	            }
	        });
	    };
	    FingerprintComponent.prototype.disableButton = function (styleClass) {
	        return $("" + styleClass).attr('disabled', 'disabled');
	    };
	    FingerprintComponent.prototype.enableButton = function (styleClass) {
	        return $("" + styleClass).removeAttr('disabled');
	    };
	    FingerprintComponent.prototype.fingerValueToName = function (value) {
	        var finger = value.toLowerCase().split('_');
	        var res = '';
	        finger.forEach(function (f) {
	            res += f.charAt(0).toUpperCase() + f.slice(1) + " ";
	        });
	        return res.trim();
	    };
	    FingerprintComponent.prototype.attachStyle = function () {
	        if (!$('#dialog_style').length) {
	            this.body.append("\n\t\t<style id=\"dialog_style\">\n      \t\tZebraDialog_Buttons button, .ZebraDialog_Title {\n    \t\t\tfont-family: Roboto,sans-serif;\n    \t\t\tfont-size: 16px;\n    \t\t\tline-height: 24px;\n\t\t\t}\n\n\t\t\t.ZebraDialog_Buttons button {\n    \t\t\t display: inline-block;\n    white-space: nowrap;\n    zoom: 1;\n    *display: inline;\n    -webkit-border-radius: 6px;\n    -moz-border-radius: 6px;\n    border-radius: 6px;\n    color: #fff;\n    font-weight: 700;\n    margin-right: 5px!important;\n    min-width: 60px;\n    padding: 10px 15px;\n    text-align: center;\n    text-decoration: none;\n    text-shadow: 1px 0 2px #222;\n    _width: 60px;\n    background-color: #006dcc;\n    *background-color: #04c;\n    background-image: -moz-linear-gradient(top,#08c,#04c);\n    background-image: -webkit-gradient(linear,0 0,0 100%,from(#08c),to(#04c));\n    background-image: -webkit-linear-gradient(top,#08c,#04c);\n    background-image: -o-linear-gradient(top,#08c,#04c);\n    background-image: linear-gradient(to bottom,#08c,#04c);\n    background-repeat: repeat-x;\n    border-color: rgba(0,0,0,.1) rgba(0,0,0,.1) rgba(0,0,0,.25);\n    filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#FF0088CC', endColorstr='#FF0044CC', GradientType=0);\n    filter: progid:DXImageTransform.Microsoft.gradient(enabled=false)\n\t\t\t}\n\t\t\t.ZebraDialog_Buttons button:hover, button:disabled {\n    background: #224467;\n    color: #fff\n}\n\t\t</style>\n\t\t");
	        }
	    };
	    return FingerprintComponent;
	}());

	exports.FingerprintComponent = FingerprintComponent;
	window.FingerprintComponent = FingerprintComponent;

	Object.defineProperty(exports, '__esModule', { value: true });

}));
