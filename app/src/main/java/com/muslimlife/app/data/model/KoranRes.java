package com.muslimlife.app.data.model;

public class KoranRes {

    private String page;
    private int surahNumber;
    private String symbols;
    private int startAyahPosition;

    public KoranRes(String page, String symbols){
        this.page = page;
        this.symbols = symbols;

        cleanPage();
    }

    public KoranRes(int testType){

        //todo remove test data
        surahNumber = 2;
        switch (testType){
            case 1:
                surahNumber = 1;
                page = "\uF100 \uF101 \uF102 \uF103 \uF104\n" +
                        "\uF105 \uF106 \uF107 \uF108 \uF109\n" +
                        "\uF10A \uF10B \uF10C \uF10D \uF10E \uF10F \uF110\n" +
                        "\uF111 \uF112 \uF113 \uF114 \uF115 \uF116\n" +
                        "\uF117 \uF118 \uF119 \uF11A \uF11B \uF11C\n" +
                        "\uF11D \uF11E \uF11F \uF120\n" +
                        "\uF121 \uF122 \uF123";

                symbols = "";
                startAyahPosition = 0;

                cleanPage();

                page = "\n " +
                        "\n " +
                        "\n " +
                        "\n " +
                        page +
                        "\n " +
                        "\n " +
                        "\n " +
                        "\n ";

                break;
            case 2:
                page = "\uF8DB\n" +
                        "\uF124 \uF125 \uF126 \uF127 \uF128 \uF129\uF12A \uF12B\uF12C \uF12D\n" +
                        "\uF12E \uF12F \uF130 \uF131 \uF132 \uF133 \uF134\n" +
                        "\uF135 \uF136 \uF137 \uF138 \uF139 \uF13A \uF13B \uF13C\n" +
                        "\uF13D \uF13E \uF13F \uF140 \uF141 \uF142 \uF143 \uF144 \uF145\n" +
                        "\uF146 \uF147 \uF148 \uF149 \uF14A\uF14B \uF14C\n" +
                        "\uF14D \uF14E \uF14F";

                symbols = "";
                startAyahPosition = 0;

                cleanPage();

                page = "\n " +
                        "\n " +
                        "\n " +
                        "\n " +
                        page +
                        "\n " +
                        "\n " +
                        "\n " +
                        "\n ";
                break;
            case 3:
                page = " \n" +
                        "\uF150 \uF151 \uF152 \uF153 \uF154 \uF155 \uF156 \uF157 \uF158\n" +
                        "\n" +
                        "\uF159 \uF15A \uF15B \uF15C \uF15D \uF15E \uF15F \uF160 \uF161\uF162 \uF163\n" +
                        "\n" +
                        "\uF164 \uF165\uF166 \uF167 \uF168 \uF169 \uF16A \uF16B \uF16C\n" +
                        "\n" +
                        "\uF16D \uF16E \uF16F \uF170 \uF171 \uF172 \uF173 \uF174 \uF175 \uF176\n" +
                        "\n" +
                        "\uF177 \uF178 \uF179 \uF17A \uF17B \uF17C \uF17D \uF17E\n" +
                        "\n" +
                        "\uF17F \uF180 \uF181 \uF182 \uF183 \uF184 \uF185 \uF186 \uF187\uF188\n" +
                        "\n" +
                        "\uF189 \uF18A \uF18B \uF18C \uF18D \uF18E \uF18F \uF190 \uF191 \uF192\n" +
                        "\n" +
                        "\uF193 \uF194 \uF195 \uF196 \uF197 \uF198 \uF199 \uF19A \uF19B \uF19C \uF19D\n" +
                        "\n" +
                        "\uF19E \uF19F \uF1A0 \uF1A1 \uF1A2 \uF1A3 \uF1A4 \uF1A5 \uF1A6\n" +
                        "\n" +
                        "\uF1A7 \uF1A8 \uF1A9 \uF1AA \uF1AB \uF1AC \uF1AD \uF1AE \uF1AF\uF1B0\n" +
                        "\n" +
                        "\uF1B1 \uF1B2 \uF1B3 \uF1B4 \uF1B5 \uF1B6 \uF1B7 \uF1B8 \uF1B9 \uF1BA\n" +
                        "\n" +
                        "\uF1BB \uF1BC \uF1BD \uF1BE \uF1BF \uF1C0 \uF1C1 \uF1C2 \uF1C3 \uF1C4\n" +
                        "\n" +
                        "\uF1C5 \uF1C6 \uF1C7 \uF1C8 \uF1C9 \uF1CA \uF1CB \uF1CC \uF1CD\n" +
                        "\n" +
                        "\uF1CE \uF1CF \uF1D0 \uF1D1 \uF1D2 \uF1D3 \uF1D4 \uF1D5\n" +
                        "\n" +
                        "\uF1D6 \uF1D7 \uF1D8 \uF1D9 \uF1DA \uF1DB \uF1DC \uF1DD\n";

                symbols = "";
                startAyahPosition = 5;

                cleanPage();
                break;
            case 4:
                page = "\uF1DE \uF1DF \uF1E0 \uF1E1 \uF1E2 \uF1E3 \uF1E4 \uF1E5 \uF1E6\n" +
                        "\n" +
                        "\uF1E7 \uF1E8 \uF1E9 \uF1EA \uF1EB \uF1EC \uF1ED \uF1EE \uF1EF \uF1F0\n" +
                        "\n" +
                        "\uF1F1 \uF1F2 \uF1F3 \uF1F4 \uF1F5 \uF1F6 \uF1F7 \uF1F8 \uF1F9 \uF1FA \uF1FB\n" +
                        "\n" +
                        "\uF1FC \uF1FD \uF1FE \uF1FF \uF200 \uF201 \uF202 \uF203\n" +
                        "\n" +
                        "\uF204 \uF205 \uF206\uF207 \uF208 \uF209 \uF20A \uF20B \uF20C \uF20D\n" +
                        "\n" +
                        "\uF20E \uF20F\uF210 \uF211 \uF212 \uF213 \uF214 \uF215 \uF216 \uF217 \uF218\n" +
                        "\n" +
                        "\uF219\uF21A \uF21B \uF21C \uF21D \uF21E \uF21F \uF220\uF221 \uF222 \uF223 \uF224\n" +
                        "\n" +
                        "\uF225 \uF226 \uF227 \uF228 \uF229 \uF22A \uF22B \uF22C \uF22D\n" +
                        "\n" +
                        "\uF22E \uF22F \uF230 \uF231 \uF232 \uF233 \uF234 \uF235 \uF236 \uF237\n" +
                        "\n" +
                        "\uF238 \uF239 \uF23A \uF23B \uF23C \uF23D \uF23E \uF23F \uF240\n" +
                        "\n" +
                        "\uF241 \uF242 \uF243 \uF244 \uF245\uF246 \uF247 \uF248 \uF249 \uF24A \uF24B\n" +
                        "\n" +
                        "\uF24C \uF24D \uF24E \uF24F \uF250 \uF251 \uF252 \uF253 \uF254 \uF255 \uF256\n" +
                        "\n" +
                        "\uF257 \uF258 \uF259 \uF25A \uF25B \uF25C \uF25D \uF25E \uF25F\n" +
                        "\n" +
                        "\uF260 \uF261 \uF262 \uF263 \uF264 \uF265 \uF266 \uF267 \uF268 \uF269\n" +
                        "\n" +
                        "\uF26A \uF26B \uF26C \uF26D\uF26E \uF26F \uF270 \uF271\n";

                symbols = "";
                startAyahPosition = 16;

                cleanPage();
                break;
            case 5:

                page = "\uF272 \uF273 \uF274 \uF275 \uF276 \uF277 \uF278 \uF279\n" +
                        "\n" +
                        "\uF27A \uF27B \uF27C \uF27D\uF27E \uF27F \uF280 \uF281 \uF282 \uF283\n" +
                        "\n" +
                        "\uF284 \uF285 \uF286 \uF287 \uF288 \uF289 \uF28A\uF28B \uF28C \uF28D \uF28E\uF28F\n" +
                        "\n" +
                        "\uF290 \uF291 \uF292 \uF293\uF294 \uF295 \uF296 \uF297 \uF298 \uF299 \uF29A\n" +
                        "\n" +
                        "\uF29B \uF29C \uF29D \uF29E \uF29F \uF2A0 \uF2A1 \uF2A2 \uF2A3 \uF2A4\uF2A5 \uF2A6\n" +
                        "\n" +
                        "\uF2A7 \uF2A8 \uF2A9 \uF2AA \uF2AB \uF2AC \uF2AD\uF2AE \uF2AF\n" +
                        "\n" +
                        "\uF2B0 \uF2B1 \uF2B2 \uF2B3 \uF2B4 \uF2B5 \uF2B6 \uF2B7\uF2B8\n" +
                        "\n" +
                        "\uF2B9 \uF2BA \uF2BB \uF2BC \uF2BD \uF2BE\uF2BF \uF2C0 \uF2C1 \uF2C2\n" +
                        "\n" +
                        "\uF2C3 \uF2C4 \uF2C5 \uF2C6 \uF2C7 \uF2C8 \uF2C9 \uF2CA \uF2CB\n" +
                        "\n" +
                        "\uF2CC \uF2CD \uF2CE \uF2CF \uF2D0 \uF2D1 \uF2D2 \uF2D3 \uF2D4\n" +
                        "\n" +
                        "\uF2D5 \uF2D6\uF2D7 \uF2D8 \uF2D9 \uF2DA \uF2DB \uF2DC\n" +
                        "\n" +
                        "\uF2DD \uF2DE \uF2DF \uF2E0 \uF2E1\uF2E2 \uF2E3 \uF2E4\n" +
                        "\n" +
                        "\uF2E5 \uF2E6 \uF2E7 \uF2E8 \uF2E9 \uF2EA \uF2EB \uF2EC \uF2ED\n" +
                        "\n" +
                        "\uF2EE \uF2EF \uF2F0 \uF2F1 \uF2F2 \uF2F3 \uF2F4 \uF2F5 \uF2F6\n" +
                        "\n" +
                        "\uF2F7 \uF2F8 \uF2F9\uF2FA \uF2FB \uF2FC \uF2FD \uF2FE \uF2FF\n";

                //page = page.replace(" ", "");

                symbols = "\uF298\uF2C5\uF2DB\uF2EA\uF2FF";
                startAyahPosition = 24;

                cleanPage();
                break;
            case 6:

                page =" \n" +
                        "\uF300 \uF301 \uF302 \uF303 \uF304 \uF305 \uF306 \uF307 \uF308\uF309 \uF30A\n" +
                        "\n" +
                        "\uF30B \uF30C \uF30D \uF30E \uF30F \uF310 \uF311 \uF312 \uF313\n" +
                        "\n" +
                        "\uF314 \uF315 \uF316\uF317 \uF318 \uF319 \uF31A \uF31B \uF31C \uF31D \uF31E \uF31F\n" +
                        "\n" +
                        "\uF320 \uF321 \uF322 \uF323 \uF324 \uF325 \uF326 \uF327\n" +
                        "\n" +
                        "\uF328 \uF329 \uF32A \uF32B \uF32C \uF32D \uF32E \uF32F \uF330\n" +
                        "\n" +
                        "\uF331 \uF332 \uF333 \uF334 \uF335 \uF336\uF337 \uF338 \uF339 \uF33A \uF33B \uF33C \uF33D \uF33E\n" +
                        "\n" +
                        "\uF33F \uF340\uF341 \uF342 \uF343 \uF344 \uF345 \uF346 \uF347\n" +
                        "\n" +
                        "\uF348 \uF349 \uF34A \uF34B \uF34C \uF34D \uF34E \uF34F \uF350\n" +
                        "\n" +
                        "\uF351 \uF352 \uF353 \uF354 \uF355 \uF356 \uF357 \uF358 \uF359\n" +
                        "\n" +
                        "\uF35A \uF35B \uF35C \uF35D \uF35E \uF35F \uF360 \uF361 \uF362 \uF363\n" +
                        "\n" +
                        "\uF364 \uF365 \uF366 \uF367 \uF368 \uF369 \uF36A \uF36B \uF36C\n" +
                        "\n" +
                        "\uF36D \uF36E \uF36F \uF370 \uF371 \uF372 \uF373 \uF374 \uF375 \uF376\n" +
                        "\n" +
                        "\uF377 \uF378 \uF379 \uF37A \uF37B \uF37C\uF37D \uF37E \uF37F \uF380\n" +
                        "\n" +
                        "\uF381 \uF382\uF383 \uF384 \uF385 \uF386 \uF387 \uF388 \uF389 \uF38A \uF38B \uF38C\n" +
                        "\n" +
                        "\uF38D \uF38E \uF38F \uF390 \uF391 \uF392\uF393 \uF394 \uF395 \uF396 \uF397 \uF398\n";

                symbols = "\uF31E\uF32E\uF33C\uF354\uF362\uF375\uF38B\uF398";
                startAyahPosition = 29;

                cleanPage();
                break;
            case 7:
                page =" \n" +
                        "\uF399 \uF39A \uF39B \uF39C\uF39D \uF39E \uF39F \uF3A0 \uF3A1 \uF3A2 \uF3A3\n" +
                        "\n" +
                        "\uF3A4 \uF3A5 \uF3A6 \uF3A7 \uF3A8 \uF3A9 \uF3AA \uF3AB \uF3AC \uF3AD\n" +
                        "\n" +
                        "\uF3AE \uF3AF \uF3B0 \uF3B1 \uF3B2\uF3B3 \uF3B4 \uF3B5 \uF3B6 \uF3B7\n" +
                        "\n" +
                        "\uF3B8 \uF3B9 \uF3BA \uF3BB \uF3BC \uF3BD \uF3BE \uF3BF \uF3C0\n" +
                        "\n" +
                        "\uF3C1 \uF3C2 \uF3C3 \uF3C4 \uF3C5 \uF3C6 \uF3C7 \uF3C8 \uF3C9\n" +
                        "\n" +
                        "\uF3CA \uF3CB \uF3CC \uF3CD \uF3CE \uF3CF \uF3D0\uF3D1 \uF3D2 \uF3D3 \uF3D4\n" +
                        "\n" +
                        "\uF3D5 \uF3D6 \uF3D7 \uF3D8 \uF3D9 \uF3DA \uF3DB \uF3DC \uF3DD \uF3DE\n" +
                        "\n" +
                        "\uF3DF \uF3E0 \uF3E1 \uF3E2 \uF3E3 \uF3E4 \uF3E5 \uF3E6\n" +
                        "\n" +
                        "\uF3E7 \uF3E8 \uF3E9 \uF3EA \uF3EB \uF3EC \uF3ED \uF3EE\n" +
                        "\n" +
                        "\uF3EF \uF3F0 \uF3F1 \uF3F2 \uF3F3\uF3F4 \uF3F5 \uF3F6 \uF3F7\n" +
                        "\n" +
                        "\uF3F8 \uF3F9 \uF3FA\uF3FB \uF3FC \uF3FD \uF3FE \uF3FF \uF400 \uF401\n" +
                        "\n" +
                        "\uF402 \uF403 \uF404 \uF405 \uF406 \uF407 \uF408 \uF409 \uF40A\n" +
                        "\n" +
                        "\uF40B \uF40C \uF40D \uF40E \uF40F \uF410 \uF411 \uF412 \uF413\n" +
                        "\n" +
                        "\uF414 \uF415 \uF416 \uF417 \uF418 \uF419 \uF41A \uF41B \uF41C \uF41D \uF41E\n" +
                        "\n" +
                        "\uF41F \uF420 \uF421 \uF422 \uF423 \uF424 \uF425 \uF426 \uF427 \uF428 \uF429 \uF42A\n";

                symbols = "\uF3AB\uF3B7\uF3C5\uF3D9\uF3E2\uF3EA\uF3F7\uF401\uF40A\uF416\uF42A";
                startAyahPosition = 37;

                cleanPage();
                break;
            case 8:
                page =" \n" +
                        "\uF42B \uF42C \uF42D \uF42E \uF42F \uF430 \uF431 \uF432\n" +
                        "\n" +
                        "\uF433 \uF434 \uF435 \uF436\uF437 \uF438 \uF439 \uF43A\n" +
                        "\n" +
                        "\uF43B \uF43C \uF43D \uF43E \uF43F \uF440 \uF441 \uF442 \uF443\n" +
                        "\n" +
                        "\uF444 \uF445 \uF446 \uF447 \uF448 \uF449 \uF44A \uF44B \uF44C\n" +
                        "\n" +
                        "\uF44D \uF44E \uF44F \uF450 \uF451 \uF452 \uF453 \uF454 \uF455 \uF456\n" +
                        "\n" +
                        "\uF457 \uF458 \uF459 \uF45A \uF45B \uF45C \uF45D \uF45E \uF45F\n" +
                        "\n" +
                        "\uF460 \uF461 \uF462 \uF463 \uF464 \uF465 \uF466 \uF467\n" +
                        "\n" +
                        "\uF468 \uF469 \uF46A \uF46B \uF46C \uF46D \uF46E \uF46F \uF470\n" +
                        "\n" +
                        "\uF471 \uF472 \uF473 \uF474 \uF475 \uF476 \uF477\n" +
                        "\n" +
                        "\uF478 \uF479 \uF47A \uF47B \uF47C \uF47D\uF47E \uF47F \uF480 \uF481\n" +
                        "\n" +
                        "\uF482 \uF483 \uF484 \uF485 \uF486 \uF487 \uF488 \uF489 \uF48A \uF48B \uF48C\n" +
                        "\n" +
                        "\uF48D \uF48E \uF48F \uF490 \uF491 \uF492 \uF493 \uF494\n" +
                        "\n" +
                        "\uF495 \uF496 \uF497 \uF498 \uF499 \uF49A \uF49B \uF49C\n" +
                        "\n" +
                        "\uF49D \uF49E \uF49F \uF4A0 \uF4A1\uF4A2 \uF4A3 \uF4A4 \uF4A5\n" +
                        "\n" +
                        "\uF4A6 \uF4A7\uF4A8 \uF4A9 \uF4AA \uF4AB \uF4AC \uF4AD \uF4AE \uF4AF\n";

                symbols = "\uF43E\uF449\uF456\uF45F\uF467\uF483\uF492\uF49A\uF4AF";
                startAyahPosition = 48;

                cleanPage();
                break;
            case 9:
                page =" \n" +
                        "\uF4B0 \uF4B1 \uF4B2 \uF4B3 \uF4B4 \uF4B5 \uF4B6 \uF4B7 \uF4B8\n" +
                        "\n" +
                        "\uF4B9 \uF4BA \uF4BB \uF4BC \uF4BD \uF4BE \uF4BF \uF4C0\n" +
                        "\n" +
                        "\uF4C1\uF4C2 \uF4C3 \uF4C4 \uF4C5 \uF4C6 \uF4C7\n" +
                        "\n" +
                        "\uF4C8 \uF4C9 \uF4CA \uF4CB \uF4CC \uF4CD \uF4CE \uF4CF \uF4D0 \uF4D1\n" +
                        "\n" +
                        "\uF4D2 \uF4D3 \uF4D4 \uF4D5 \uF4D6 \uF4D7 \uF4D8 \uF4D9 \uF4DA \uF4DB\n" +
                        "\n" +
                        "\uF4DC \uF4DD \uF4DE \uF4DF \uF4E0 \uF4E1\uF4E2 \uF4E3\n" +
                        "\n" +
                        "\uF4E4 \uF4E5 \uF4E6 \uF4E7\uF4E8 \uF4E9 \uF4EA \uF4EB \uF4EC \uF4ED\uF4EE \uF4EF\n" +
                        "\n" +
                        "\uF4F0 \uF4F1 \uF4F2 \uF4F3 \uF4F4 \uF4F5 \uF4F6 \uF4F7 \uF4F8 \uF4F9\n" +
                        "\n" +
                        "\uF4FA \uF4FB \uF4FC \uF4FD \uF4FE \uF4FF \uF500 \uF501 \uF502 \uF503\n" +
                        "\n" +
                        "\uF504 \uF505 \uF506 \uF507 \uF508 \uF509 \uF50A \uF50B \uF50C\n" +
                        "\n" +
                        "\uF50D \uF50E \uF50F\uF510 \uF511 \uF512 \uF513 \uF514\n" +
                        "\n" +
                        "\uF515 \uF516 \uF517 \uF518\uF519 \uF51A \uF51B \uF51C \uF51D \uF51E \uF51F\uF520\n" +
                        "\n" +
                        "\uF521 \uF522 \uF523 \uF524 \uF525 \uF526 \uF527\n" +
                        "\n" +
                        "\uF528\uF529 \uF52A \uF52B \uF52C \uF52D \uF52E \uF52F \uF530\n" +
                        "\n" +
                        "\uF531 \uF532 \uF533\uF534 \uF535 \uF536 \uF537 \uF538 \uF539 \uF53A\n";

                symbols = "\uF4C5\uF4D8\uF4F9\uF53A";
                startAyahPosition = 57;

                cleanPage();
                break;
            case 10:
                page ="\uF53B \uF53C \uF53D \uF53E \uF53F \uF540 \uF541 \uF542\n" +
                        "\n" +
                        "\uF543 \uF544 \uF545 \uF546 \uF547 \uF548 \uF549 \uF54A \uF54B\n" +
                        "\n" +
                        "\uF54C \uF54D \uF54E \uF54F \uF550 \uF551 \uF552 \uF553 \uF554 \uF555\n" +
                        "\n" +
                        "\uF556 \uF557 \uF558 \uF559 \uF55A \uF55B \uF55C\n" +
                        "\n" +
                        "\uF55D \uF55E \uF55F \uF560 \uF561 \uF562 \uF563 \uF564 \uF565\n" +
                        "\n" +
                        "\uF566 \uF567 \uF568\uF569 \uF56A \uF56B \uF56C \uF56D \uF56E \uF56F \uF570\n" +
                        "\n" +
                        "\uF571 \uF572 \uF573 \uF574 \uF575 \uF576 \uF577 \uF578 \uF579\n" +
                        "\n" +
                        "\uF57A \uF57B \uF57C \uF57D \uF57E \uF57F \uF580 \uF581 \uF582\n" +
                        "\n" +
                        "\uF583 \uF584 \uF585 \uF586 \uF587 \uF588 \uF589 \uF58A \uF58B\n" +
                        "\n" +
                        "\uF58C \uF58D \uF58E \uF58F \uF590 \uF591 \uF592 \uF593\uF594 \uF595\n" +
                        "\n" +
                        "\uF596 \uF597\uF598 \uF599 \uF59A \uF59B \uF59C \uF59D \uF59E \uF59F \uF5A0\n" +
                        "\n" +
                        "\uF5A1 \uF5A2 \uF5A3 \uF5A4 \uF5A5 \uF5A6 \uF5A7 \uF5A8\uF5A9 \uF5AA \uF5AB \uF5AC \uF5AD\n" +
                        "\n" +
                        "\uF5AE \uF5AF \uF5B0 \uF5B1 \uF5B2 \uF5B3 \uF5B4 \uF5B5\uF5B6 \uF5B7 \uF5B8\n" +
                        "\n" +
                        "\uF5B9 \uF5BA \uF5BB \uF5BC \uF5BD \uF5BE \uF5BF \uF5C0 \uF5C1 \uF5C2\uF5C3 \uF5C4 \uF5C5\n" +
                        "\n" +
                        "\uF5C6 \uF5C7 \uF5C8 \uF5C9 \uF5CA \uF5CB \uF5CC \uF5CD \uF5CE\n";

                symbols = "\uF553\uF563\uF572\uF57F\uF589\uF5A0\uF5BA\uF5CE";
                startAyahPosition = 61;

                cleanPage();
                break;
            case 600:

                page = " \n" +
                        "\uF166\n" +
                        "\n" +
                        "\uF8DC\n" +
                        "\n" +
                        "\uF5EE \uF5EF \uF5F0 \uF5F1 \uF5F2 \uF5F3 \uF5F4 \uF5F5 \uF5F6 \uF5F7\n" +
                        "\n" +
                        "\uF5F8 \uF5F9 \uF5FA \uF5FB \uF5FC \uF5FD \uF5FE\n" +
                        "\n" +
                        "\uF167\n" +
                        "\n" +
                        "\uF8DC\n" +
                        "\n" +
                        "\uF5FF \uF608 \uF609 \uF60A \uF60B \uF60C \uF60D \uF60E \uF60F \uF610\n" +
                        "\n" +
                        "\uF611 \uF612 \uF613 \uF614 \uF615 \uF616\uF617 \uF618 \uF619 \uF61A \uF61B\n" +
                        "\n" +
                        "\uF61C \uF61D \uF61E \uF61F \uF620 \uF621 \uF622 \uF623 \uF624 \uF625 \uF626\n" +
                        "\n" +
                        "\uF627 \uF628 \uF629 \uF62A \uF62B \uF62C \uF62D \uF62E \uF62F \uF630 \uF631\n" +
                        "\n" +
                        "\uF168\n" +
                        "\n" +
                        "\uF8DC\n" +
                        "\n" +
                        "\uF632 \uF633 \uF634 \uF635 \uF636 \uF637 \uF638 \uF639 \uF63A \uF63B\n" +
                        "\n" +
                        "\uF63C \uF63D \uF63E \uF63F \uF640 \uF641 \uF642 \uF643 \uF644\n" +
                        "\n" +
                        "\uF645 \uF646 \uF647 \uF648 \uF649 \uF653 \uF654 \uF655 \uF656\n" +
                        " ";

                symbols = "\uF649\uF656\uF63F\uF644\uF639\uF629\uF62D\uF631\uF620\uF624\uF615\uF5F4\uF5EF\uF5FE\uF610\uF60B\uF61B";

                startAyahPosition = 61;

                cleanPage();
                break;
        }
    }

    private void cleanPage(){
        page = page.replace(" ", "");

        String[] lines = page.split("\n");
        StringBuilder pageBuilder = new StringBuilder();

        for(int i = 0; i < lines.length; i++) {
            if (!lines[i].isEmpty()) {
                pageBuilder.append(lines[i]);
                if(i != lines.length - 1)
                    pageBuilder.append("\n");
            }
        }

        page = pageBuilder.toString();
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getSymbols() {
        return symbols;
    }

    public int getStartAyahPosition() {
        return startAyahPosition;
    }

    public int getSurahNumber() {
        return surahNumber;
    }
}
