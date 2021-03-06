package com.pro.bityard.entity;

import java.io.Serializable;
import java.util.List;

public class CountryCodeEntity implements Serializable {


    /**
     * code : 200
     * data : [{"code":null,"countryCode":244,"currency":null,"id":"1","language":null,"nameCn":"安哥拉","nameEn":"Angola"},{"code":null,"countryCode":93,"currency":null,"id":"2","language":null,"nameCn":"阿富汗","nameEn":"Afghanistan"},{"code":null,"countryCode":355,"currency":null,"id":"3","language":null,"nameCn":"阿尔巴尼亚","nameEn":"Albania"},{"code":null,"countryCode":213,"currency":null,"id":"4","language":null,"nameCn":"阿尔及利亚","nameEn":"Algeria"},{"code":null,"countryCode":376,"currency":null,"id":"5","language":null,"nameCn":"安道尔共和国","nameEn":"Andorra"},{"code":null,"countryCode":1264,"currency":null,"id":"6","language":null,"nameCn":"安圭拉岛","nameEn":"Anguilla"},{"code":null,"countryCode":1268,"currency":null,"id":"7","language":null,"nameCn":"安提瓜和巴布达","nameEn":"Antigua and Barbuda"},{"code":null,"countryCode":54,"currency":null,"id":"8","language":null,"nameCn":"阿根廷","nameEn":"Argentina"},{"code":null,"countryCode":374,"currency":null,"id":"9","language":null,"nameCn":"亚美尼亚","nameEn":"Armenia"},{"code":null,"countryCode":247,"currency":null,"id":"10","language":null,"nameCn":"阿森松","nameEn":"Ascension"},{"code":null,"countryCode":61,"currency":null,"id":"11","language":null,"nameCn":"澳大利亚","nameEn":"Australia"},{"code":null,"countryCode":43,"currency":null,"id":"12","language":null,"nameCn":"奥地利","nameEn":"Austria"},{"code":null,"countryCode":994,"currency":null,"id":"13","language":null,"nameCn":"阿塞拜疆","nameEn":"Azerbaijan"},{"code":null,"countryCode":1242,"currency":null,"id":"14","language":null,"nameCn":"巴哈马","nameEn":"Bahamas"},{"code":null,"countryCode":973,"currency":null,"id":"15","language":null,"nameCn":"巴林","nameEn":"Bahrain"},{"code":null,"countryCode":880,"currency":null,"id":"16","language":null,"nameCn":"孟加拉国","nameEn":"Bangladesh"},{"code":null,"countryCode":1246,"currency":null,"id":"17","language":null,"nameCn":"巴巴多斯","nameEn":"Barbados"},{"code":null,"countryCode":375,"currency":null,"id":"18","language":null,"nameCn":"白俄罗斯","nameEn":"Belarus"},{"code":null,"countryCode":32,"currency":null,"id":"19","language":null,"nameCn":"比利时","nameEn":"Belgium"},{"code":null,"countryCode":501,"currency":null,"id":"20","language":null,"nameCn":"伯利兹","nameEn":"Belize"},{"code":null,"countryCode":229,"currency":null,"id":"21","language":null,"nameCn":"贝宁","nameEn":"Benin"},{"code":null,"countryCode":1441,"currency":null,"id":"22","language":null,"nameCn":"百慕大群岛","nameEn":"Bermuda"},{"code":null,"countryCode":591,"currency":null,"id":"23","language":null,"nameCn":"玻利维亚","nameEn":"Bolivia"},{"code":null,"countryCode":267,"currency":null,"id":"24","language":null,"nameCn":"博茨瓦纳","nameEn":"Botswana"},{"code":null,"countryCode":55,"currency":null,"id":"25","language":null,"nameCn":"巴西","nameEn":"Brazil"},{"code":null,"countryCode":673,"currency":null,"id":"26","language":null,"nameCn":"文莱","nameEn":"Brunei"},{"code":null,"countryCode":359,"currency":null,"id":"27","language":null,"nameCn":"保加利亚","nameEn":"Bulgaria"},{"code":null,"countryCode":226,"currency":null,"id":"28","language":null,"nameCn":"布基纳法索","nameEn":"Burkina Faso"},{"code":null,"countryCode":95,"currency":null,"id":"29","language":null,"nameCn":"缅甸","nameEn":"Myanmar"},{"code":null,"countryCode":257,"currency":null,"id":"30","language":null,"nameCn":"布隆迪","nameEn":"Burundi"},{"code":null,"countryCode":237,"currency":null,"id":"31","language":null,"nameCn":"喀麦隆","nameEn":"Cameroon"},{"code":null,"countryCode":1,"currency":null,"id":"32","language":null,"nameCn":"加拿大","nameEn":"Canada"},{"code":null,"countryCode":1345,"currency":null,"id":"33","language":null,"nameCn":"开曼群岛","nameEn":"Cayman Islands"},{"code":null,"countryCode":236,"currency":null,"id":"34","language":null,"nameCn":"中非共和国","nameEn":"Central African Republic"},{"code":null,"countryCode":235,"currency":null,"id":"35","language":null,"nameCn":"乍得","nameEn":"Chad"},{"code":null,"countryCode":56,"currency":null,"id":"36","language":null,"nameCn":"智利","nameEn":"Chile"},{"code":null,"countryCode":86,"currency":null,"id":"37","language":null,"nameCn":"中国","nameEn":"China"},{"code":null,"countryCode":57,"currency":null,"id":"38","language":null,"nameCn":"哥伦比亚","nameEn":"Colombia"},{"code":null,"countryCode":243,"currency":null,"id":"39","language":null,"nameCn":"刚果","nameEn":"Congo"},{"code":null,"countryCode":682,"currency":null,"id":"40","language":null,"nameCn":"库克群岛","nameEn":"Cook Islands."},{"code":null,"countryCode":506,"currency":null,"id":"41","language":null,"nameCn":"哥斯达黎加","nameEn":"Costa Rica"},{"code":null,"countryCode":53,"currency":null,"id":"42","language":null,"nameCn":"古巴","nameEn":"Cuba"},{"code":null,"countryCode":357,"currency":null,"id":"43","language":null,"nameCn":"塞浦路斯","nameEn":"Cyprus"},{"code":null,"countryCode":420,"currency":null,"id":"44","language":null,"nameCn":"捷克","nameEn":"Czech Republic"},{"code":null,"countryCode":45,"currency":null,"id":"45","language":null,"nameCn":"丹麦","nameEn":"Denmark"},{"code":null,"countryCode":253,"currency":null,"id":"46","language":null,"nameCn":"吉布提","nameEn":"Djibouti"},{"code":null,"countryCode":1809,"currency":null,"id":"47","language":null,"nameCn":"多米尼加共和国","nameEn":"Dominican Republic"},{"code":null,"countryCode":593,"currency":null,"id":"48","language":null,"nameCn":"厄瓜多尔","nameEn":"Ecuador"},{"code":null,"countryCode":20,"currency":null,"id":"49","language":null,"nameCn":"埃及","nameEn":"Egypt"},{"code":null,"countryCode":503,"currency":null,"id":"50","language":null,"nameCn":"萨尔瓦多","nameEn":"El Salvador"},{"code":null,"countryCode":372,"currency":null,"id":"51","language":null,"nameCn":"爱沙尼亚","nameEn":"Estonia"},{"code":null,"countryCode":251,"currency":null,"id":"52","language":null,"nameCn":"埃塞俄比亚","nameEn":"Ethiopia"},{"code":null,"countryCode":679,"currency":null,"id":"53","language":null,"nameCn":"斐济","nameEn":"Fiji"},{"code":null,"countryCode":358,"currency":null,"id":"54","language":null,"nameCn":"芬兰","nameEn":"Finland"},{"code":null,"countryCode":33,"currency":null,"id":"55","language":null,"nameCn":"法国","nameEn":"France"},{"code":null,"countryCode":594,"currency":null,"id":"56","language":null,"nameCn":"法属圭亚那","nameEn":"French Guiana"},{"code":null,"countryCode":241,"currency":null,"id":"57","language":null,"nameCn":"加蓬","nameEn":"Gabon"},{"code":null,"countryCode":220,"currency":null,"id":"58","language":null,"nameCn":"冈比亚","nameEn":"Gambia"},{"code":null,"countryCode":995,"currency":null,"id":"59","language":null,"nameCn":"格鲁吉亚","nameEn":"Georgia"},{"code":null,"countryCode":49,"currency":null,"id":"60","language":null,"nameCn":"德国","nameEn":"Germany"},{"code":null,"countryCode":233,"currency":null,"id":"61","language":null,"nameCn":"加纳","nameEn":"Ghana"},{"code":null,"countryCode":350,"currency":null,"id":"62","language":null,"nameCn":"直布罗陀","nameEn":"Gibraltar"},{"code":null,"countryCode":30,"currency":null,"id":"63","language":null,"nameCn":"希腊","nameEn":"Greece"},{"code":null,"countryCode":1473,"currency":null,"id":"64","language":null,"nameCn":"格林纳达","nameEn":"Grenada"},{"code":null,"countryCode":1671,"currency":null,"id":"65","language":null,"nameCn":"关岛","nameEn":"Guam"},{"code":null,"countryCode":502,"currency":null,"id":"66","language":null,"nameCn":"危地马拉","nameEn":"Guatemala"},{"code":null,"countryCode":224,"currency":null,"id":"67","language":null,"nameCn":"几内亚","nameEn":"Guinea"},{"code":null,"countryCode":592,"currency":null,"id":"68","language":null,"nameCn":"圭亚那","nameEn":"Guyana"},{"code":null,"countryCode":509,"currency":null,"id":"69","language":null,"nameCn":"海地","nameEn":"Haiti"},{"code":null,"countryCode":504,"currency":null,"id":"70","language":null,"nameCn":"洪都拉斯","nameEn":"Honduras"},{"code":null,"countryCode":852,"currency":null,"id":"71","language":null,"nameCn":"中国香港","nameEn":"Hong Kong (China)"},{"code":null,"countryCode":36,"currency":null,"id":"72","language":null,"nameCn":"匈牙利","nameEn":"Hungary"},{"code":null,"countryCode":354,"currency":null,"id":"73","language":null,"nameCn":"冰岛","nameEn":"Iceland"},{"code":null,"countryCode":91,"currency":null,"id":"74","language":null,"nameCn":"印度","nameEn":"India"},{"code":null,"countryCode":62,"currency":null,"id":"75","language":null,"nameCn":"印度尼西亚","nameEn":"Indonesia"},{"code":null,"countryCode":98,"currency":null,"id":"76","language":null,"nameCn":"伊朗","nameEn":"Iran"},{"code":null,"countryCode":964,"currency":null,"id":"77","language":null,"nameCn":"伊拉克","nameEn":"Iraq"},{"code":null,"countryCode":353,"currency":null,"id":"78","language":null,"nameCn":"爱尔兰","nameEn":"Ireland"},{"code":null,"countryCode":972,"currency":null,"id":"79","language":null,"nameCn":"以色列","nameEn":"Israel"},{"code":null,"countryCode":39,"currency":null,"id":"80","language":null,"nameCn":"意大利","nameEn":"Italy"},{"code":null,"countryCode":225,"currency":null,"id":"81","language":null,"nameCn":"科特迪瓦","nameEn":"Ivory Coast"},{"code":null,"countryCode":1876,"currency":null,"id":"82","language":null,"nameCn":"牙买加","nameEn":"Jamaica"},{"code":null,"countryCode":81,"currency":null,"id":"83","language":null,"nameCn":"日本","nameEn":"Japan"},{"code":null,"countryCode":962,"currency":null,"id":"84","language":null,"nameCn":"约旦","nameEn":"Jordan"},{"code":null,"countryCode":855,"currency":null,"id":"85","language":null,"nameCn":"柬埔寨","nameEn":"Cambodia"},{"code":null,"countryCode":7,"currency":null,"id":"86","language":null,"nameCn":"哈萨克斯坦","nameEn":"Kazakhstan"},{"code":null,"countryCode":254,"currency":null,"id":"87","language":null,"nameCn":"肯尼亚","nameEn":"Kenya"},{"code":null,"countryCode":82,"currency":null,"id":"88","language":null,"nameCn":"韩国","nameEn":"Korea"},{"code":null,"countryCode":965,"currency":null,"id":"89","language":null,"nameCn":"科威特","nameEn":"Kuwait"},{"code":null,"countryCode":996,"currency":null,"id":"90","language":null,"nameCn":"吉尔吉斯坦","nameEn":"Kyrgyzstan"},{"code":null,"countryCode":856,"currency":null,"id":"91","language":null,"nameCn":"老挝","nameEn":"Laos"},{"code":null,"countryCode":371,"currency":null,"id":"92","language":null,"nameCn":"拉脱维亚","nameEn":"Latvia"},{"code":null,"countryCode":961,"currency":null,"id":"93","language":null,"nameCn":"黎巴嫩","nameEn":"Lebanon"},{"code":null,"countryCode":266,"currency":null,"id":"94","language":null,"nameCn":"莱索托","nameEn":"Lesotho"},{"code":null,"countryCode":231,"currency":null,"id":"95","language":null,"nameCn":"利比里亚","nameEn":"Liberia"},{"code":null,"countryCode":218,"currency":null,"id":"96","language":null,"nameCn":"利比亚","nameEn":"Libya"},{"code":null,"countryCode":423,"currency":null,"id":"97","language":null,"nameCn":"列支敦士登","nameEn":"Liechtenstein"},{"code":null,"countryCode":370,"currency":null,"id":"98","language":null,"nameCn":"立陶宛","nameEn":"Lithuania"},{"code":null,"countryCode":352,"currency":null,"id":"99","language":null,"nameCn":"卢森堡","nameEn":"Luxembourg"},{"code":null,"countryCode":853,"currency":null,"id":"100","language":null,"nameCn":"中国澳门","nameEn":"Macao (China)"},{"code":null,"countryCode":261,"currency":null,"id":"101","language":null,"nameCn":"马达加斯加","nameEn":"Madagascar"},{"code":null,"countryCode":265,"currency":null,"id":"102","language":null,"nameCn":"马拉维","nameEn":"Malawi"},{"code":null,"countryCode":60,"currency":null,"id":"103","language":null,"nameCn":"马来西亚","nameEn":"Malaysia"},{"code":null,"countryCode":960,"currency":null,"id":"104","language":null,"nameCn":"马尔代夫","nameEn":"Maldives"},{"code":null,"countryCode":223,"currency":null,"id":"105","language":null,"nameCn":"马里","nameEn":"Mali"},{"code":null,"countryCode":356,"currency":null,"id":"106","language":null,"nameCn":"马耳他","nameEn":"Malta"},{"code":null,"countryCode":223,"currency":null,"id":"107","language":null,"nameCn":"马里亚那群岛","nameEn":"Mariana Islands"},{"code":null,"countryCode":596,"currency":null,"id":"108","language":null,"nameCn":"马提尼克","nameEn":"Martinique"},{"code":null,"countryCode":230,"currency":null,"id":"109","language":null,"nameCn":"毛里求斯","nameEn":"Mauritius"},{"code":null,"countryCode":52,"currency":null,"id":"110","language":null,"nameCn":"墨西哥","nameEn":"Mexico"},{"code":null,"countryCode":373,"currency":null,"id":"111","language":null,"nameCn":"摩尔多瓦","nameEn":"Moldova"},{"code":null,"countryCode":377,"currency":null,"id":"112","language":null,"nameCn":"摩纳哥","nameEn":"Monaco"},{"code":null,"countryCode":976,"currency":null,"id":"113","language":null,"nameCn":"蒙古","nameEn":"Mongolia"},{"code":null,"countryCode":1664,"currency":null,"id":"114","language":null,"nameCn":"蒙特塞拉特岛","nameEn":"Montserrat"},{"code":null,"countryCode":212,"currency":null,"id":"115","language":null,"nameCn":"摩洛哥","nameEn":"Morocco"},{"code":null,"countryCode":258,"currency":null,"id":"116","language":null,"nameCn":"莫桑比克","nameEn":"Mozambique"},{"code":null,"countryCode":264,"currency":null,"id":"117","language":null,"nameCn":"纳米比亚","nameEn":"Namibia"},{"code":null,"countryCode":674,"currency":null,"id":"118","language":null,"nameCn":"瑙鲁","nameEn":"Nauru"},{"code":null,"countryCode":977,"currency":null,"id":"119","language":null,"nameCn":"尼泊尔","nameEn":"Nepal"},{"code":null,"countryCode":599,"currency":null,"id":"120","language":null,"nameCn":"荷属安的列斯","nameEn":"Netheriands Antilles"},{"code":null,"countryCode":31,"currency":null,"id":"121","language":null,"nameCn":"荷兰","nameEn":"Netherlands"},{"code":null,"countryCode":64,"currency":null,"id":"122","language":null,"nameCn":"新西兰","nameEn":"New Zealand"},{"code":null,"countryCode":505,"currency":null,"id":"123","language":null,"nameCn":"尼加拉瓜","nameEn":"Nicaragua"},{"code":null,"countryCode":227,"currency":null,"id":"124","language":null,"nameCn":"尼日尔","nameEn":"Niger"},{"code":null,"countryCode":234,"currency":null,"id":"125","language":null,"nameCn":"尼日利亚","nameEn":"Nigeria"},{"code":null,"countryCode":850,"currency":null,"id":"126","language":null,"nameCn":"朝鲜","nameEn":"North Korea"},{"code":null,"countryCode":47,"currency":null,"id":"127","language":null,"nameCn":"挪威","nameEn":"Norway"},{"code":null,"countryCode":968,"currency":null,"id":"128","language":null,"nameCn":"阿曼","nameEn":"Oman"},{"code":null,"countryCode":92,"currency":null,"id":"129","language":null,"nameCn":"巴基斯坦","nameEn":"Pakistan"},{"code":null,"countryCode":507,"currency":null,"id":"130","language":null,"nameCn":"巴拿马","nameEn":"Panama"},{"code":null,"countryCode":675,"currency":null,"id":"131","language":null,"nameCn":"巴布亚新几内亚","nameEn":"Papua New Guinea"},{"code":null,"countryCode":595,"currency":null,"id":"132","language":null,"nameCn":"巴拉圭","nameEn":"Paraguay"},{"code":null,"countryCode":51,"currency":null,"id":"133","language":null,"nameCn":"秘鲁","nameEn":"Peru"},{"code":null,"countryCode":63,"currency":null,"id":"134","language":null,"nameCn":"菲律宾","nameEn":"Philippines"},{"code":null,"countryCode":48,"currency":null,"id":"135","language":null,"nameCn":"波兰","nameEn":"Poland"},{"code":null,"countryCode":689,"currency":null,"id":"136","language":null,"nameCn":"法属玻利尼西亚","nameEn":"French Polynesia"},{"code":null,"countryCode":351,"currency":null,"id":"137","language":null,"nameCn":"葡萄牙","nameEn":"Portugal"},{"code":null,"countryCode":1,"currency":null,"id":"138","language":null,"nameCn":"波多黎各","nameEn":"Puerto Rico"},{"code":null,"countryCode":974,"currency":null,"id":"139","language":null,"nameCn":"卡塔尔","nameEn":"Qatar"},{"code":null,"countryCode":262,"currency":null,"id":"140","language":null,"nameCn":"留尼旺","nameEn":"Reunion"},{"code":null,"countryCode":40,"currency":null,"id":"141","language":null,"nameCn":"罗马尼亚","nameEn":"Romania"},{"code":null,"countryCode":7,"currency":null,"id":"142","language":null,"nameCn":"俄罗斯","nameEn":"Russia"},{"code":null,"countryCode":1758,"currency":null,"id":"143","language":null,"nameCn":"圣卢西亚","nameEn":"Saint Lucia"},{"code":null,"countryCode":1784,"currency":null,"id":"144","language":null,"nameCn":"圣文森特岛","nameEn":"Saint Vincent"},{"code":null,"countryCode":684,"currency":null,"id":"145","language":null,"nameCn":"东萨摩亚(美)","nameEn":"Samoa Eastern"},{"code":null,"countryCode":685,"currency":null,"id":"146","language":null,"nameCn":"西萨摩亚","nameEn":"Samoa Western"},{"code":null,"countryCode":378,"currency":null,"id":"147","language":null,"nameCn":"圣马力诺","nameEn":"San Marino"},{"code":null,"countryCode":239,"currency":null,"id":"148","language":null,"nameCn":"圣多美和普林西比","nameEn":"Sao Tome and Principe"},{"code":null,"countryCode":966,"currency":null,"id":"149","language":null,"nameCn":"沙特阿拉伯","nameEn":"Saudi Arabia"},{"code":null,"countryCode":221,"currency":null,"id":"150","language":null,"nameCn":"塞内加尔","nameEn":"Senegal"},{"code":null,"countryCode":248,"currency":null,"id":"151","language":null,"nameCn":"塞舌尔","nameEn":"Seychelles"},{"code":null,"countryCode":232,"currency":null,"id":"152","language":null,"nameCn":"塞拉利昂","nameEn":"Sierra Leone"},{"code":null,"countryCode":65,"currency":null,"id":"153","language":null,"nameCn":"新加坡","nameEn":"Singapore"},{"code":null,"countryCode":421,"currency":null,"id":"154","language":null,"nameCn":"斯洛伐克","nameEn":"Slovakia"},{"code":null,"countryCode":386,"currency":null,"id":"155","language":null,"nameCn":"斯洛文尼亚","nameEn":"Slovenia"},{"code":null,"countryCode":677,"currency":null,"id":"156","language":null,"nameCn":"所罗门群岛","nameEn":"Solomon Islands"},{"code":null,"countryCode":252,"currency":null,"id":"157","language":null,"nameCn":"索马里","nameEn":"Somalia"},{"code":null,"countryCode":27,"currency":null,"id":"158","language":null,"nameCn":"南非","nameEn":"South Africa"},{"code":null,"countryCode":34,"currency":null,"id":"159","language":null,"nameCn":"西班牙","nameEn":"Spain"},{"code":null,"countryCode":94,"currency":null,"id":"160","language":null,"nameCn":"斯里兰卡","nameEn":"Sri Lanka"},{"code":null,"countryCode":1758,"currency":null,"id":"161","language":null,"nameCn":"圣卢西亚","nameEn":"St.Lucia"},{"code":null,"countryCode":1784,"currency":null,"id":"162","language":null,"nameCn":"圣文森特","nameEn":"St.Vincent"},{"code":null,"countryCode":249,"currency":null,"id":"163","language":null,"nameCn":"苏丹","nameEn":"Sudan"},{"code":null,"countryCode":597,"currency":null,"id":"164","language":null,"nameCn":"苏里南","nameEn":"Suriname"},{"code":null,"countryCode":268,"currency":null,"id":"165","language":null,"nameCn":"斯威士兰","nameEn":"Swaziland"},{"code":null,"countryCode":46,"currency":null,"id":"166","language":null,"nameCn":"瑞典","nameEn":"Sweden"},{"code":null,"countryCode":41,"currency":null,"id":"167","language":null,"nameCn":"瑞士","nameEn":"Switzerland"},{"code":null,"countryCode":963,"currency":null,"id":"168","language":null,"nameCn":"叙利亚","nameEn":"Syria"},{"code":null,"countryCode":886,"currency":null,"id":"169","language":null,"nameCn":"中国台湾","nameEn":"Taiwan (China)"},{"code":null,"countryCode":992,"currency":null,"id":"170","language":null,"nameCn":"塔吉克斯坦","nameEn":"Tajikistan"},{"code":null,"countryCode":255,"currency":null,"id":"171","language":null,"nameCn":"坦桑尼亚","nameEn":"Tanzania"},{"code":null,"countryCode":66,"currency":null,"id":"172","language":null,"nameCn":"泰国","nameEn":"Thailand"},{"code":null,"countryCode":228,"currency":null,"id":"173","language":null,"nameCn":"多哥","nameEn":"Togo"},{"code":null,"countryCode":676,"currency":null,"id":"174","language":null,"nameCn":"汤加","nameEn":"Tonga"},{"code":null,"countryCode":1868,"currency":null,"id":"175","language":null,"nameCn":"特立尼达和多巴哥","nameEn":"Trinidad and Tobago"},{"code":null,"countryCode":216,"currency":null,"id":"176","language":null,"nameCn":"突尼斯","nameEn":"Tunisia"},{"code":null,"countryCode":90,"currency":null,"id":"177","language":null,"nameCn":"土耳其","nameEn":"Turkey"},{"code":null,"countryCode":993,"currency":null,"id":"178","language":null,"nameCn":"土库曼斯坦","nameEn":"Turkmenistan"},{"code":null,"countryCode":256,"currency":null,"id":"179","language":null,"nameCn":"乌干达","nameEn":"Uganda"},{"code":null,"countryCode":380,"currency":null,"id":"180","language":null,"nameCn":"乌克兰","nameEn":"Ukraine"},{"code":null,"countryCode":971,"currency":null,"id":"181","language":null,"nameCn":"阿拉伯联合酋长国","nameEn":"United Arab Emirates"},{"code":null,"countryCode":44,"currency":null,"id":"182","language":null,"nameCn":"英国","nameEn":"United Kingdom"},{"code":null,"countryCode":1,"currency":null,"id":"183","language":null,"nameCn":"美国","nameEn":"United States of America"},{"code":null,"countryCode":598,"currency":null,"id":"184","language":null,"nameCn":"乌拉圭","nameEn":"Uruguay"},{"code":null,"countryCode":998,"currency":null,"id":"185","language":null,"nameCn":"乌兹别克斯坦","nameEn":"Uzbekistan"},{"code":null,"countryCode":58,"currency":null,"id":"186","language":null,"nameCn":"委内瑞拉","nameEn":"Venezuela"},{"code":null,"countryCode":84,"currency":null,"id":"187","language":null,"nameCn":"越南","nameEn":"Vietnam"},{"code":null,"countryCode":967,"currency":null,"id":"188","language":null,"nameCn":"也门","nameEn":"Yemen"},{"code":null,"countryCode":338,"currency":null,"id":"189","language":null,"nameCn":"南斯拉夫","nameEn":"Yugoslavia"},{"code":null,"countryCode":263,"currency":null,"id":"191","language":null,"nameCn":"津巴布韦","nameEn":"Zimbabwe"},{"code":null,"countryCode":243,"currency":null,"id":"192","language":null,"nameCn":"扎伊尔","nameEn":"Zaire"},{"code":null,"countryCode":260,"currency":null,"id":"193","language":null,"nameCn":"赞比亚","nameEn":"Zambia"},{"code":null,"countryCode":297,"currency":null,"id":"194","language":null,"nameCn":"阿鲁巴","nameEn":"Aruba"},{"code":null,"countryCode":672,"currency":null,"id":"195","language":null,"nameCn":"澳大利亚海外领地","nameEn":"Australian overseas territories"},{"code":null,"countryCode":975,"currency":null,"id":"196","language":null,"nameCn":"不丹","nameEn":"Bhutan"},{"code":null,"countryCode":387,"currency":null,"id":"197","language":null,"nameCn":"波斯尼亚和黑塞哥维那","nameEn":"Bosnia and Herzegovina"},{"code":null,"countryCode":238,"currency":null,"id":"198","language":null,"nameCn":"佛得角","nameEn":"Cape Verde"},{"code":null,"countryCode":269,"currency":null,"id":"199","language":null,"nameCn":"科摩罗群岛","nameEn":"Comoros Islands"},{"code":null,"countryCode":385,"currency":null,"id":"200","language":null,"nameCn":"克罗地亚","nameEn":"Croatia"},{"code":null,"countryCode":246,"currency":null,"id":"201","language":null,"nameCn":"迭戈加西亚群岛","nameEn":"Diego Garcia"},{"code":null,"countryCode":670,"currency":null,"id":"202","language":null,"nameCn":"东帝汶","nameEn":"East Timor"},{"code":null,"countryCode":240,"currency":null,"id":"203","language":null,"nameCn":"赤道几内亚","nameEn":"Equatorial Guinea"},{"code":null,"countryCode":291,"currency":null,"id":"204","language":null,"nameCn":"厄立特里亚","nameEn":"Eritrea"},{"code":null,"countryCode":500,"currency":null,"id":"205","language":null,"nameCn":"福克兰群岛","nameEn":"Falkland Islands"},{"code":null,"countryCode":298,"currency":null,"id":"206","language":null,"nameCn":"法罗群岛","nameEn":"Faroe Islands"},{"code":null,"countryCode":299,"currency":null,"id":"207","language":null,"nameCn":"格陵兰岛","nameEn":"Greenland"},{"code":null,"countryCode":590,"currency":null,"id":"208","language":null,"nameCn":"瓜德罗普","nameEn":"Guadeloupe"},{"code":null,"countryCode":245,"currency":null,"id":"209","language":null,"nameCn":"几内亚比绍","nameEn":"Guinea-Bissau"},{"code":null,"countryCode":686,"currency":null,"id":"210","language":null,"nameCn":"基里巴斯","nameEn":"Kiribati"},{"code":null,"countryCode":389,"currency":null,"id":"211","language":null,"nameCn":"马其顿","nameEn":"Macedonia"},{"code":null,"countryCode":692,"currency":null,"id":"212","language":null,"nameCn":"马绍尔群岛","nameEn":"Marshall Islands"},{"code":null,"countryCode":222,"currency":null,"id":"213","language":null,"nameCn":"毛里塔尼亚","nameEn":"Mauritania"},{"code":null,"countryCode":691,"currency":null,"id":"214","language":null,"nameCn":"密克罗尼西亚","nameEn":"Micronesia"},{"code":null,"countryCode":382,"currency":null,"id":"215","language":null,"nameCn":"黑山","nameEn":"Montenegro"},{"code":null,"countryCode":687,"currency":null,"id":"216","language":null,"nameCn":"新喀里多尼亚","nameEn":"New Caledonia"},{"code":null,"countryCode":683,"currency":null,"id":"217","language":null,"nameCn":"纽埃岛","nameEn":"Niue"},{"code":null,"countryCode":680,"currency":null,"id":"218","language":null,"nameCn":"帕劳","nameEn":"Palau"},{"code":null,"countryCode":970,"currency":null,"id":"219","language":null,"nameCn":"巴勒斯坦","nameEn":"Palestine"},{"code":null,"countryCode":250,"currency":null,"id":"220","language":null,"nameCn":"卢旺达","nameEn":"Rwanda"},{"code":null,"countryCode":290,"currency":null,"id":"221","language":null,"nameCn":"圣赫勒拿岛","nameEn":"St.Helena"},{"code":null,"countryCode":508,"currency":null,"id":"222","language":null,"nameCn":"圣皮埃尔和密克隆群岛","nameEn":"Saint Pierre and Miquelon"},{"code":null,"countryCode":381,"currency":null,"id":"223","language":null,"nameCn":"塞尔维亚","nameEn":"Serbia"},{"code":null,"countryCode":690,"currency":null,"id":"224","language":null,"nameCn":"托克劳群岛","nameEn":"Tokelau"},{"code":null,"countryCode":688,"currency":null,"id":"225","language":null,"nameCn":"图瓦卢","nameEn":"Tuvalu"},{"code":null,"countryCode":678,"currency":null,"id":"226","language":null,"nameCn":"瓦努阿图","nameEn":"Vanuatu"},{"code":null,"countryCode":379,"currency":null,"id":"227","language":null,"nameCn":"梵蒂冈城","nameEn":"Vatican City"},{"code":null,"countryCode":681,"currency":null,"id":"228","language":null,"nameCn":"瓦利斯和富图纳","nameEn":"Wallis and Futuna"},{"code":null,"countryCode":1284,"currency":null,"id":"229","language":null,"nameCn":"英属维尔京群岛","nameEn":"British Virgin Islands"},{"code":null,"countryCode":9714,"currency":null,"id":"230","language":null,"nameCn":"迪拜酋长国","nameEn":"Emirate of Dubai"}]
     * countryCode : 60
     * message :
     */

    private String code;
    private String countryCode;
    private String message;
    private List<DataBean> data;

    @Override
    public String toString() {
        return "CountryCodeEntity{" +
                "code='" + code + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * code : null
         * countryCode : 244
         * currency : null
         * id : 1
         * language : null
         * nameCn : 安哥拉
         * nameEn : Angola
         */

        private Object code;
        private String countryCode;
        private Object currency;
        private String id;
        private Object language;
        private String nameCn;
        private String nameEn;
        private boolean isUsed;

        @Override
        public String toString() {
            return "DataBean{" +
                    "code=" + code +
                    ", countryCode='" + countryCode + '\'' +
                    ", currency=" + currency +
                    ", id='" + id + '\'' +
                    ", language=" + language +
                    ", nameCn='" + nameCn + '\'' +
                    ", nameEn='" + nameEn + '\'' +
                    ", isUsed=" + isUsed +
                    '}';
        }

        public boolean isUsed() {
            return isUsed;
        }

        public void setUsed(boolean used) {
            isUsed = used;
        }



        public Object getCode() {
            return code;
        }

        public void setCode(Object code) {
            this.code = code;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public Object getCurrency() {
            return currency;
        }

        public void setCurrency(Object currency) {
            this.currency = currency;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Object getLanguage() {
            return language;
        }

        public void setLanguage(Object language) {
            this.language = language;
        }

        public String getNameCn() {
            return nameCn;
        }

        public void setNameCn(String nameCn) {
            this.nameCn = nameCn;
        }

        public String getNameEn() {
            return nameEn;
        }

        public void setNameEn(String nameEn) {
            this.nameEn = nameEn;
        }
    }
}
