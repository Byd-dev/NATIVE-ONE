package com.pro.bityard.entity;

import com.pro.bityard.utils.TradeUtil;

import java.util.List;

public class TradeHistoryEntity {


    /**
     * code : 200
     * data : [{"commodity":"达世币","commodityCode":"DASHUSDT","commodityType":"FT","contCode":"DASHUSDT1808","contract":"DASHUSDT1808","contractCode":"DASHUSDT1808","cpPrice":82.39,"cpVolume":1.2182,"currency":"USDT","deferDays":0,"deferFee":0.038,"eagleDeduction":0,"followId":"0","id":"461594970168492032","income":-0.36546,"investUserId":"0","investUsername":null,"isBuy":false,"lever":20,"margin":5,"moneyType":0,"opPrice":82.09,"opVolume":1.2182,"orVolume":0,"price":82,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":0.1425,"shared":false,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1587982448000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587983746000,"volume":1.2182},{"commodity":"达世币","commodityCode":"DASHUSDT","commodityType":"FT","contCode":"DASHUSDT1808","contract":"DASHUSDT1808","contractCode":"DASHUSDT1808","cpPrice":82.39,"cpVolume":1.2194,"currency":"USDT","deferDays":0,"deferFee":0.038,"eagleDeduction":0,"followId":"0","id":"461594255698165760","income":-0.463372,"investUserId":"0","investUsername":null,"isBuy":false,"lever":20,"margin":5,"moneyType":0,"opPrice":82.01,"opVolume":1.2194,"orVolume":0,"price":8,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":0.1425,"shared":false,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1587982288000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587983746000,"volume":1.2194},{"commodity":"达世币","commodityCode":"DASHUSDT","commodityType":"FT","contCode":"DASHUSDT1808","contract":"DASHUSDT1808","contractCode":"DASHUSDT1808","cpPrice":82.28,"cpVolume":1.2177,"currency":"USDT","deferDays":0,"deferFee":0.038,"eagleDeduction":0,"followId":"0","id":"461594002567725056","income":0.194832,"investUserId":"0","investUsername":null,"isBuy":true,"lever":20,"margin":5,"moneyType":0,"opPrice":82.12,"opVolume":1.2177,"orVolume":0,"price":85,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":0.1425,"shared":false,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1587982222000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587983746000,"volume":1.2177},{"commodity":"达世币","commodityCode":"DASHUSDT","commodityType":"FT","contCode":"DASHUSDT1808","contract":"DASHUSDT1808","contractCode":"DASHUSDT1808","cpPrice":82.28,"cpVolume":1.2177,"currency":"USDT","deferDays":0,"deferFee":0.038,"eagleDeduction":0,"followId":"0","id":"461594361889554432","income":0.194832,"investUserId":"0","investUsername":null,"isBuy":true,"lever":20,"margin":5,"moneyType":0,"opPrice":82.12,"opVolume":1.2177,"orVolume":0,"price":85,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":0.1425,"shared":false,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1587982325000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587983746000,"volume":1.2177},{"commodity":"达世币","commodityCode":"DASHUSDT","commodityType":"FT","contCode":"DASHUSDT1808","contract":"DASHUSDT1808","contractCode":"DASHUSDT1808","cpPrice":82.39,"cpVolume":1.2195,"currency":"USDT","deferDays":0,"deferFee":0.038,"eagleDeduction":0,"followId":"0","id":"461594084042080256","income":-0.475605,"investUserId":"0","investUsername":null,"isBuy":false,"lever":20,"margin":5,"moneyType":0,"opPrice":82,"opVolume":1.2195,"orVolume":0,"price":8,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":0.1425,"shared":false,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1587982263000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587983746000,"volume":1.2195},{"commodity":"达世币","commodityCode":"DASHUSDT","commodityType":"FT","contCode":"DASHUSDT1808","contract":"DASHUSDT1808","contractCode":"DASHUSDT1808","cpPrice":82.03,"cpVolume":1.2173,"currency":"USDT","deferDays":0,"deferFee":0.038,"eagleDeduction":0,"followId":"0","id":"461593880609947648","income":-0.146076,"investUserId":"0","investUsername":null,"isBuy":true,"lever":20,"margin":5,"moneyType":0,"opPrice":82.15,"opVolume":1.2173,"orVolume":0,"price":85,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":0.1425,"shared":false,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1587982189000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587982196000,"volume":1.2173},{"commodity":"达世币","commodityCode":"DASHUSDT","commodityType":"FT","contCode":"DASHUSDT1808","contract":"DASHUSDT1808","contractCode":"DASHUSDT1808","cpPrice":82.13,"cpVolume":1.2176,"currency":"USDT","deferDays":0,"deferFee":0.038,"eagleDeduction":0,"followId":"0","id":"461592578819620864","income":0,"investUserId":"0","investUsername":null,"isBuy":true,"lever":20,"margin":5,"moneyType":0,"opPrice":82.13,"opVolume":1.2176,"orVolume":0,"price":85,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":0.1425,"shared":false,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1587981880000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587981888000,"volume":1.2176},{"commodity":"达世币","commodityCode":"DASHUSDT","commodityType":"FT","contCode":"DASHUSDT1808","contract":"DASHUSDT1808","contractCode":"DASHUSDT1808","cpPrice":82.14,"cpVolume":1.2168,"currency":"USDT","deferDays":0,"deferFee":0.038,"eagleDeduction":0,"followId":"0","id":"461592459374231552","income":-0.048672,"investUserId":"0","investUsername":null,"isBuy":true,"lever":20,"margin":5,"moneyType":0,"opPrice":82.18,"opVolume":1.2168,"orVolume":0,"price":85,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":0.1425,"shared":false,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1587981846000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587981859000,"volume":1.2168},{"commodity":"达世币","commodityCode":"DASHUSDT","commodityType":"FT","contCode":"DASHUSDT1808","contract":"DASHUSDT1808","contractCode":"DASHUSDT1808","cpPrice":82.23,"cpVolume":1.2176,"currency":"USDT","deferDays":0,"deferFee":0.038,"eagleDeduction":0,"followId":"0","id":"461581900889194496","income":0.12176,"investUserId":"0","investUsername":null,"isBuy":true,"lever":20,"margin":5,"moneyType":0,"opPrice":82.13,"opVolume":1.2176,"orVolume":0,"price":85,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":0.1425,"shared":false,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1587979337000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587980558000,"volume":1.2176},{"commodity":"达世币","commodityCode":"DASHUSDT","commodityType":"FT","contCode":"DASHUSDT1808","contract":"DASHUSDT1808","contractCode":"DASHUSDT1808","cpPrice":82.23,"cpVolume":1.2176,"currency":"USDT","deferDays":0,"deferFee":0.038,"eagleDeduction":0,"followId":"0","id":"461581790612553728","income":0.12176,"investUserId":"0","investUsername":null,"isBuy":true,"lever":20,"margin":5,"moneyType":0,"opPrice":82.13,"opVolume":1.2176,"orVolume":0,"price":85,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":0.1425,"shared":false,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1587979337000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587980558000,"volume":1.2176},{"commodity":"达世币","commodityCode":"DASHUSDT","commodityType":"FT","contCode":"DASHUSDT1808","contract":"DASHUSDT1808","contractCode":"DASHUSDT1808","cpPrice":82.12,"cpVolume":1.2177,"currency":"USDT","deferDays":0,"deferFee":0.038,"eagleDeduction":0,"followId":"0","id":"461581688376393728","income":0,"investUserId":"0","investUsername":null,"isBuy":true,"lever":20,"margin":5,"moneyType":0,"opPrice":82.12,"opVolume":1.2177,"orVolume":0,"price":85,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":0.1425,"shared":false,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1587979289000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587979300000,"volume":1.2177},{"commodity":"达世币","commodityCode":"DASHUSDT","commodityType":"FT","contCode":"DASHUSDT1808","contract":"DASHUSDT1808","contractCode":"DASHUSDT1808","cpPrice":82.13,"cpVolume":1.2162,"currency":"USDT","deferDays":0,"deferFee":0.038,"eagleDeduction":0,"followId":"0","id":"461581590842048512","income":-0.109458,"investUserId":"0","investUsername":null,"isBuy":true,"lever":20,"margin":5,"moneyType":0,"opPrice":82.22,"opVolume":1.2162,"orVolume":0,"price":82.22,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":0.1425,"shared":false,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1587979227000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587979268000,"volume":1.2162},{"commodity":"以太坊","commodityCode":"ETHUSDT","commodityType":"FT","contCode":"ETHUSDT1808","contract":"ETHUSDT1808","contractCode":"ETHUSDT1808","cpPrice":194.99,"cpVolume":2.0514,"currency":"USDT","deferDays":0,"deferFee":0.17775,"eagleDeduction":0,"followId":"0","id":"461581474118762496","income":0,"investUserId":"0","investUsername":null,"isBuy":true,"lever":80,"margin":5,"moneyType":0,"opPrice":194.99,"opVolume":2.0514,"orVolume":0,"price":200,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":0.5925,"shared":false,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1587979233000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587979247000,"volume":2.0514},{"commodity":"以太坊","commodityCode":"ETHUSDT","commodityType":"FT","contCode":"ETHUSDT1808","contract":"ETHUSDT1808","contractCode":"ETHUSDT1808","cpPrice":194.95,"cpVolume":40.9899,"currency":"USDT","deferDays":0,"deferFee":3.555,"eagleDeduction":0,"followId":"0","id":"461580941035307008","income":-9.017778,"investUserId":"0","investUsername":null,"isBuy":true,"lever":80,"margin":100,"moneyType":0,"opPrice":195.17,"opVolume":40.9899,"orVolume":0,"price":200,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":11.85,"shared":false,"stopLoss":-90,"stopLossBegin":-90,"stopProfit":300,"stopProfitBegin":300,"time":1587979110000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587979221000,"volume":40.9899},{"commodity":"以太坊","commodityCode":"ETHUSDT","commodityType":"FT","contCode":"ETHUSDT1808","contract":"ETHUSDT1808","contractCode":"ETHUSDT1808","cpPrice":194.91,"cpVolume":2.0499,"currency":"USDT","deferDays":0,"deferFee":0.17775,"eagleDeduction":0,"followId":"0","id":"461581081544491008","income":-0.450978,"investUserId":"0","investUsername":null,"isBuy":true,"lever":80,"margin":5,"moneyType":0,"opPrice":195.13,"opVolume":2.0499,"orVolume":0,"price":200,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":0.5925,"shared":false,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1587979149000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587979218000,"volume":2.0499},{"commodity":"以太坊","commodityCode":"ETHUSDT","commodityType":"FT","contCode":"ETHUSDT1808","contract":"ETHUSDT1808","contractCode":"ETHUSDT1808","cpPrice":195.29,"cpVolume":40.9731,"currency":"USDT","deferDays":0,"deferFee":3.555,"eagleDeduction":0,"followId":"0","id":"461580807308312576","income":1.638924,"investUserId":"0","investUsername":null,"isBuy":true,"lever":80,"margin":100,"moneyType":0,"opPrice":195.25,"opVolume":40.9731,"orVolume":0,"price":200,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":11.85,"shared":false,"stopLoss":-90,"stopLossBegin":-90,"stopProfit":300,"stopProfitBegin":300,"time":1587979076000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587979094000,"volume":40.9731},{"commodity":"以太坊","commodityCode":"ETHUSDT","commodityType":"FT","contCode":"ETHUSDT1808","contract":"ETHUSDT1808","contractCode":"ETHUSDT1808","cpPrice":195.28,"cpVolume":40.9668,"currency":"USDT","deferDays":0,"deferFee":3.555,"eagleDeduction":0,"followId":"0","id":"461580689452564480","income":0,"investUserId":"0","investUsername":null,"isBuy":true,"lever":80,"margin":100,"moneyType":0,"opPrice":195.28,"opVolume":40.9668,"orVolume":0,"price":200,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":11.85,"shared":false,"stopLoss":-90,"stopLossBegin":-90,"stopProfit":300,"stopProfitBegin":300,"time":1587979054000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587979074000,"volume":40.9668},{"commodity":"以太坊","commodityCode":"ETHUSDT","commodityType":"FT","contCode":"ETHUSDT1808","contract":"ETHUSDT1808","contractCode":"ETHUSDT1808","cpPrice":195.11,"cpVolume":41.0109,"currency":"USDT","deferDays":0,"deferFee":3.555,"eagleDeduction":0,"followId":"0","id":"461579110775259136","income":1.640436,"investUserId":"0","investUsername":null,"isBuy":true,"lever":80,"margin":100,"moneyType":0,"opPrice":195.07,"opVolume":41.0109,"orVolume":0,"price":200,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":11.85,"shared":false,"stopLoss":-90,"stopLossBegin":-90,"stopProfit":300,"stopProfitBegin":300,"time":1587978672000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587978767000,"volume":41.0109},{"commodity":"以太坊","commodityCode":"ETHUSDT","commodityType":"FT","contCode":"ETHUSDT1808","contract":"ETHUSDT1808","contractCode":"ETHUSDT1808","cpPrice":195.19,"cpVolume":41.0067,"currency":"USDT","deferDays":0,"deferFee":3.555,"eagleDeduction":0,"followId":"0","id":"461578959750955008","income":-4.10067,"investUserId":"0","investUsername":null,"isBuy":false,"lever":80,"margin":100,"moneyType":0,"opPrice":195.09,"opVolume":41.0067,"orVolume":0,"price":8,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":11.85,"shared":false,"stopLoss":-90,"stopLossBegin":-90,"stopProfit":300,"stopProfitBegin":300,"time":1587978643000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587978766000,"volume":41.0067},{"commodity":"以太坊","commodityCode":"ETHUSDT","commodityType":"FT","contCode":"ETHUSDT1808","contract":"ETHUSDT1808","contractCode":"ETHUSDT1808","cpPrice":195.21,"cpVolume":0.5125,"currency":"USDT","deferDays":0,"deferFee":0.04275,"eagleDeduction":0,"followId":"0","id":"461576706268856320","income":0.035875,"investUserId":"0","investUsername":null,"isBuy":true,"lever":20,"margin":5,"moneyType":0,"opPrice":195.14,"opVolume":0.5125,"orVolume":0,"price":8878,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":0.1425,"shared":false,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1587978094000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587978104000,"volume":0.5125},{"commodity":"以太坊","commodityCode":"ETHUSDT","commodityType":"FT","contCode":"ETHUSDT1808","contract":"ETHUSDT1808","contractCode":"ETHUSDT1808","cpPrice":195.18,"cpVolume":0.5112,"currency":"USDT","deferDays":0,"deferFee":0.04275,"eagleDeduction":0,"followId":"0","id":"461574925791002624","income":-0.214704,"investUserId":"0","investUsername":null,"isBuy":true,"lever":20,"margin":5,"moneyType":0,"opPrice":195.6,"opVolume":0.5112,"orVolume":0,"price":195.74,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":0.1425,"shared":false,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1587977673000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587978097000,"volume":0.5112},{"commodity":"达世币","commodityCode":"DASHUSDT","commodityType":"FT","contCode":"DASHUSDT1808","contract":"DASHUSDT1808","contractCode":"DASHUSDT1808","cpPrice":82.26,"cpVolume":1.2134,"currency":"USDT","deferDays":0,"deferFee":0.038,"eagleDeduction":0,"followId":"0","id":"461574512081633280","income":0.18201,"investUserId":"0","investUsername":null,"isBuy":false,"lever":20,"margin":5,"moneyType":0,"opPrice":82.41,"opVolume":1.2134,"orVolume":0,"price":8,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":0.1425,"shared":false,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1587977593000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587978097000,"volume":1.2134},{"commodity":"达世币","commodityCode":"DASHUSDT","commodityType":"FT","contCode":"DASHUSDT1808","contract":"DASHUSDT1808","contractCode":"DASHUSDT1808","cpPrice":82.26,"cpVolume":1.2139,"currency":"USDT","deferDays":0,"deferFee":0.038,"eagleDeduction":0,"followId":"0","id":"461574303834439680","income":0.145668,"investUserId":"0","investUsername":null,"isBuy":false,"lever":20,"margin":5,"moneyType":0,"opPrice":82.38,"opVolume":1.2139,"orVolume":0,"price":8,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":0.1425,"shared":false,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1587977519000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587978097000,"volume":1.2139},{"commodity":"达世币","commodityCode":"DASHUSDT","commodityType":"FT","contCode":"DASHUSDT1808","contract":"DASHUSDT1808","contractCode":"DASHUSDT1808","cpPrice":82.15,"cpVolume":1.2182,"currency":"USDT","deferDays":0,"deferFee":0.038,"eagleDeduction":0,"followId":"0","id":"461573399630577664","income":0.073092,"investUserId":"0","investUsername":null,"isBuy":true,"lever":20,"margin":5,"moneyType":0,"opPrice":82.09,"opVolume":1.2182,"orVolume":0,"price":85,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":0.1425,"shared":false,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1587977317000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587978097000,"volume":1.2182},{"commodity":"达世币","commodityCode":"DASHUSDT","commodityType":"FT","contCode":"DASHUSDT1808","contract":"DASHUSDT1808","contractCode":"DASHUSDT1808","cpPrice":82.07,"cpVolume":1.2189,"currency":"USDT","deferDays":0,"deferFee":0.038,"eagleDeduction":0,"followId":"0","id":"461573267698745344","income":0.036567,"investUserId":"0","investUsername":null,"isBuy":true,"lever":20,"margin":5,"moneyType":0,"opPrice":82.04,"opVolume":1.2189,"orVolume":0,"price":85,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":0.1425,"shared":false,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1587977290000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587977307000,"volume":1.2189},{"commodity":"达世币","commodityCode":"DASHUSDT","commodityType":"FT","contCode":"DASHUSDT1808","contract":"DASHUSDT1808","contractCode":"DASHUSDT1808","cpPrice":82.03,"cpVolume":1.2191,"currency":"USDT","deferDays":0,"deferFee":0.038,"eagleDeduction":0,"followId":"0","id":"461573162249748480","income":0,"investUserId":"0","investUsername":null,"isBuy":true,"lever":20,"margin":5,"moneyType":0,"opPrice":82.03,"opVolume":1.2191,"orVolume":0,"price":85,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":0.1425,"shared":false,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1587977245000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587977256000,"volume":1.2191},{"commodity":"比特币","commodityCode":"BTCUSDT","commodityType":"FT","contCode":"BTCUSDT1808","contract":"BTCUSDT1808","contractCode":"BTCUSDT1808","cpPrice":7684.79,"cpVolume":0.013,"currency":"USDT","deferDays":0,"deferFee":0.038,"eagleDeduction":0,"followId":"0","id":"461561500079751168","income":0.31811,"investUserId":"0","investUsername":null,"isBuy":false,"lever":20,"margin":5,"moneyType":0,"opPrice":7709.26,"opVolume":0.013,"orVolume":0,"price":7695.31,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":0.1425,"shared":false,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1587974468000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587977206000,"volume":0.013},{"commodity":"比特币","commodityCode":"BTCUSDT","commodityType":"FT","contCode":"BTCUSDT1808","contract":"BTCUSDT1808","contractCode":"BTCUSDT1808","cpPrice":7683.4,"cpVolume":0.013,"currency":"USDT","deferDays":0,"deferFee":0.038,"eagleDeduction":0,"followId":"0","id":"461561398397239296","income":-0.15236,"investUserId":"0","investUsername":null,"isBuy":true,"lever":20,"margin":5,"moneyType":0,"opPrice":7695.12,"opVolume":0.013,"orVolume":0,"price":7695.31,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":0.1425,"shared":false,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1587977078000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587977206000,"volume":0.013},{"commodity":"比特币现金","commodityCode":"BCHUSDT","commodityType":"FT","contCode":"BCHUSDT1808","contract":"BCHUSDT1808","contractCode":"BCHUSDT1808","cpPrice":242.97,"cpVolume":0.4113,"currency":"USDT","deferDays":0,"deferFee":0.038,"eagleDeduction":0,"followId":"0","id":"461560827997061120","income":0.065808,"investUserId":"0","investUsername":null,"isBuy":false,"lever":20,"margin":5,"moneyType":0,"opPrice":243.13,"opVolume":0.4113,"orVolume":0,"price":2,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":0.1425,"shared":false,"stopLoss":-4.5,"stopLossBegin":-4.5,"stopProfit":15,"stopProfitBegin":15,"time":1587974318000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587974367000,"volume":0.4113},{"commodity":"比特币","commodityCode":"BTCUSDT","commodityType":"FT","contCode":"BTCUSDT1808","contract":"BTCUSDT1808","contractCode":"BTCUSDT1808","cpPrice":7693.55,"cpVolume":0.2602,"currency":"USDT","deferDays":0,"deferFee":0.76,"eagleDeduction":0,"followId":"0","id":"461558964849475584","income":-1.54819,"investUserId":"0","investUsername":null,"isBuy":false,"lever":20,"margin":100,"moneyType":0,"opPrice":7687.6,"opVolume":0.2602,"orVolume":0,"price":7687.16,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":2.85,"shared":false,"stopLoss":-90,"stopLossBegin":-90,"stopProfit":300,"stopProfitBegin":300,"time":1587973863000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587974279000,"volume":0.2602},{"commodity":"比特币","commodityCode":"BTCUSDT","commodityType":"FT","contCode":"BTCUSDT1808","contract":"BTCUSDT1808","contractCode":"BTCUSDT1808","cpPrice":7687.51,"cpVolume":0.2601,"currency":"USDT","deferDays":0,"deferFee":0.76,"eagleDeduction":0,"followId":"0","id":"461558840022794240","income":0.330327,"investUserId":"0","investUsername":null,"isBuy":false,"lever":20,"margin":100,"moneyType":0,"opPrice":7688.78,"opVolume":0.2601,"orVolume":0,"price":7685.16,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":2.85,"shared":false,"stopLoss":-90,"stopLossBegin":-90,"stopProfit":300,"stopProfitBegin":300,"time":1587973835000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587973850000,"volume":0.2601},{"commodity":"比特币","commodityCode":"BTCUSDT","commodityType":"FT","contCode":"BTCUSDT1808","contract":"BTCUSDT1808","contractCode":"BTCUSDT1808","cpPrice":7687.51,"cpVolume":0.2602,"currency":"USDT","deferDays":0,"deferFee":0.76,"eagleDeduction":0,"followId":"0","id":"461558877154967552","income":-0.135304,"investUserId":"0","investUsername":null,"isBuy":false,"lever":20,"margin":100,"moneyType":0,"opPrice":7686.99,"opVolume":0.2602,"orVolume":0,"price":7686.99,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":2.85,"shared":false,"stopLoss":-90,"stopLossBegin":-90,"stopProfit":300,"stopProfitBegin":300,"time":1587973811000,"tradeMode":"T0D","tradeMsg":"","tradeStatus":14,"tradeTime":1587973850000,"volume":0.2602},{"commodity":"比特币","commodityCode":"BTCUSDT","commodityType":"FT","contCode":"BTCUSDT1808","contract":"BTCUSDT1808","contractCode":"BTCUSDT1808","cpPrice":7746.47,"cpVolume":1.2909,"currency":"USDT","deferDays":0,"deferFee":0,"eagleDeduction":0,"followId":"0","id":"461494209774354432","income":-0.012909,"investUserId":"0","investUsername":null,"isBuy":true,"lever":100,"margin":100,"moneyType":0,"opPrice":7746.48,"opVolume":1.2909,"orVolume":0,"price":8000,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":14.85,"shared":false,"stopLoss":-90,"stopLossBegin":-90,"stopProfit":300,"stopProfitBegin":300,"time":1587958425000,"tradeMode":"T0","tradeMsg":"","tradeStatus":14,"tradeTime":1587958431000,"volume":1.2909},{"commodity":"比特币","commodityCode":"BTCUSDT","commodityType":"FT","contCode":"BTCUSDT1808","contract":"BTCUSDT1808","contractCode":"BTCUSDT1808","cpPrice":7733.02,"cpVolume":1.2913,"currency":"USDT","deferDays":0,"deferFee":0,"eagleDeduction":0,"followId":"0","id":"461487289168560128","income":-14.178474,"investUserId":"0","investUsername":null,"isBuy":true,"lever":100,"margin":100,"moneyType":0,"opPrice":7744,"opVolume":1.2913,"orVolume":0,"price":7744,"priceDigit":2,"priceRate":100,"priceVolume":0,"serviceCharge":14.85,"shared":false,"stopLoss":-90,"stopLossBegin":-90,"stopProfit":300,"stopProfitBegin":300,"time":1587956744000,"tradeMode":"T0","tradeMsg":"","tradeStatus":14,"tradeTime":1587956825000,"volume":1.2913}]
     * message :
     */

    private int code;
    private String message;
    private List<DataBean> data;

    @Override
    public String toString() {
        return "TradeRecordEntity{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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

    public static class DataBean {
        @Override
        public String toString() {
            return "DataBean{" +
                    "commodity='" + commodity + '\'' +
                    ", commodityCode='" + commodityCode + '\'' +
                    ", commodityType='" + commodityType + '\'' +
                    ", contCode='" + contCode + '\'' +
                    ", contract='" + contract + '\'' +
                    ", contractCode='" + contractCode + '\'' +
                    ", cpPrice=" + cpPrice +
                    ", cpVolume=" + cpVolume +
                    ", currency='" + currency + '\'' +
                    ", deferDays=" + deferDays +
                    ", deferFee=" + deferFee +
                    ", eagleDeduction=" + eagleDeduction +
                    ", followId='" + followId + '\'' +
                    ", id='" + id + '\'' +
                    ", income=" + income +
                    ", investUserId='" + investUserId + '\'' +
                    ", investUsername=" + investUsername +
                    ", isBuy=" + isBuy +
                    ", lever=" + lever +
                    ", margin=" + margin +
                    ", moneyType=" + moneyType +
                    ", opPrice=" + opPrice +
                    ", opVolume=" + opVolume +
                    ", orVolume=" + orVolume +
                    ", price=" + price +
                    ", priceDigit=" + priceDigit +
                    ", priceRate=" + priceRate +
                    ", priceVolume=" + priceVolume +
                    ", serviceCharge=" + serviceCharge +
                    ", shared=" + shared +
                    ", stopLoss=" + stopLoss +
                    ", stopLossBegin=" + stopLossBegin +
                    ", stopProfit=" + stopProfit +
                    ", stopProfitBegin=" + stopProfitBegin +
                    ", time=" + time +
                    ", tradeMode='" + tradeMode + '\'' +
                    ", tradeMsg='" + tradeMsg + '\'' +
                    ", tradeStatus=" + tradeStatus +
                    ", tradeTime=" + tradeTime +
                    ", volume=" + volume +
                    '}';
        }

        /**
         * commodity : 达世币
         * commodityCode : DASHUSDT
         * commodityType : FT
         * contCode : DASHUSDT1808
         * contract : DASHUSDT1808
         * contractCode : DASHUSDT1808
         * cpPrice : 82.39
         * cpVolume : 1.2182
         * currency : USDT
         * deferDays : 0
         * deferFee : 0.038
         * eagleDeduction : 0
         * followId : 0
         * id : 461594970168492032
         * income : -0.36546
         * investUserId : 0
         * investUsername : null
         * isBuy : false
         * lever : 20
         * margin : 5
         * moneyType : 0
         * opPrice : 82.09
         * opVolume : 1.2182
         * orVolume : 0
         * price : 82
         * priceDigit : 2
         * priceRate : 100
         * priceVolume : 0
         * serviceCharge : 0.1425
         * shared : false
         * stopLoss : -4.5
         * stopLossBegin : -4.5
         * stopProfit : 15
         * stopProfitBegin : 15
         * time : 1587982448000
         * tradeMode : T0D
         * tradeMsg :
         * tradeStatus : 14
         * tradeTime : 1587983746000
         * volume : 1.2182
         */


        private String commodity;
        private String commodityCode;
        private String commodityType;
        private String contCode;
        private String contract;
        private String contractCode;
        private double cpPrice;
        private double cpVolume;
        private String currency;
        private String deferDays;
        private String deferFee;
        private int eagleDeduction;
        private String followId;
        private String id;
        private double income;
        private String investUserId;
        private Object investUsername;
        private boolean isBuy;
        private double lever;
        private double margin;
        private int moneyType;
        private double opPrice;
        private double opVolume;
        private double orVolume;
        private double price;
        private int priceDigit;
        private double priceRate;
        private double priceVolume;
        private double serviceCharge;
        private boolean shared;
        private double stopLoss;
        private double stopLossBegin;
        private double stopProfit;
        private double stopProfitBegin;
        private long time;
        private String tradeMode;
        private String tradeMsg;
        private int tradeStatus;
        private long tradeTime;
        private double volume;

        public String getCommodity() {
            return commodity;
        }

        public void setCommodity(String commodity) {
            this.commodity = commodity;
        }

        public String getCommodityCode() {
            return commodityCode;
        }

        public void setCommodityCode(String commodityCode) {
            this.commodityCode = commodityCode;
        }

        public String getCommodityType() {
            return commodityType;
        }

        public void setCommodityType(String commodityType) {
            this.commodityType = commodityType;
        }

        public String getContCode() {
            return contCode;
        }

        public void setContCode(String contCode) {
            this.contCode = contCode;
        }

        public String getContract() {
            return contract;
        }

        public void setContract(String contract) {
            this.contract = contract;
        }

        public String getContractCode() {
            return contractCode;
        }

        public void setContractCode(String contractCode) {
            this.contractCode = contractCode;
        }

        public double getCpPrice() {
            return cpPrice;
        }

        public void setCpPrice(double cpPrice) {
            this.cpPrice = cpPrice;
        }

        public double getCpVolume() {
            return cpVolume;
        }

        public void setCpVolume(double cpVolume) {
            this.cpVolume = cpVolume;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getDeferDays() {

            return deferDays;

        }

        public void setDeferDays(String deferDays) {
            this.deferDays = deferDays;
        }

        public String getDeferFee() {
            return deferFee;
           /* if (Double.parseDouble(deferFee) == 0) {
                return "N/A";
            } else {
                return TradeUtil.getNumberFormat(Double.parseDouble(deferFee), 2);
            }*/
        }

        public void setDeferFee(String deferFee) {
            this.deferFee = deferFee;
        }

        public int getEagleDeduction() {
            return eagleDeduction;
        }

        public void setEagleDeduction(int eagleDeduction) {
            this.eagleDeduction = eagleDeduction;
        }

        public String getFollowId() {
            return followId;
        }

        public void setFollowId(String followId) {
            this.followId = followId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public double getIncome() {
            return income;
        }

        public void setIncome(double income) {
            this.income = income;
        }

        public String getInvestUserId() {
            return investUserId;
        }

        public void setInvestUserId(String investUserId) {
            this.investUserId = investUserId;
        }

        public Object getInvestUsername() {
            return investUsername;
        }

        public void setInvestUsername(Object investUsername) {
            this.investUsername = investUsername;
        }

        public boolean isIsBuy() {
            return isBuy;
        }

        public void setIsBuy(boolean isBuy) {
            this.isBuy = isBuy;
        }

        public double getLever() {
            return lever;
        }

        public void setLever(double lever) {
            this.lever = lever;
        }

        public double getMargin() {
            return margin;
        }

        public void setMargin(double margin) {
            this.margin = margin;
        }

        public int getMoneyType() {
            return moneyType;
        }

        public void setMoneyType(int moneyType) {
            this.moneyType = moneyType;
        }

        public double getOpPrice() {
            return opPrice;
        }

        public void setOpPrice(double opPrice) {
            this.opPrice = opPrice;
        }

        public double getOpVolume() {
            return opVolume;
        }

        public void setOpVolume(double opVolume) {
            this.opVolume = opVolume;
        }

        public double getOrVolume() {
            return orVolume;
        }

        public void setOrVolume(double orVolume) {
            this.orVolume = orVolume;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getPriceDigit() {
            return priceDigit;
        }

        public void setPriceDigit(int priceDigit) {
            this.priceDigit = priceDigit;
        }

        public double getPriceRate() {
            return priceRate;
        }

        public void setPriceRate(double priceRate) {
            this.priceRate = priceRate;
        }

        public double getPriceVolume() {
            return priceVolume;
        }

        public void setPriceVolume(double priceVolume) {
            this.priceVolume = priceVolume;
        }

        public double getServiceCharge() {
            return serviceCharge;
        }

        public void setServiceCharge(double serviceCharge) {
            this.serviceCharge = serviceCharge;
        }

        public boolean isShared() {
            return shared;
        }

        public void setShared(boolean shared) {
            this.shared = shared;
        }

        public double getStopLoss() {
            return stopLoss;
        }

        public void setStopLoss(double stopLoss) {
            this.stopLoss = stopLoss;
        }

        public double getStopLossBegin() {
            return stopLossBegin;
        }

        public void setStopLossBegin(double stopLossBegin) {
            this.stopLossBegin = stopLossBegin;
        }

        public double getStopProfit() {
            return stopProfit;
        }

        public void setStopProfit(double stopProfit) {
            this.stopProfit = stopProfit;
        }

        public double getStopProfitBegin() {
            return stopProfitBegin;
        }

        public void setStopProfitBegin(double stopProfitBegin) {
            this.stopProfitBegin = stopProfitBegin;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getTradeMode() {
            return tradeMode;
        }

        public void setTradeMode(String tradeMode) {
            this.tradeMode = tradeMode;
        }

        public String getTradeMsg() {
            return tradeMsg;
        }

        public void setTradeMsg(String tradeMsg) {
            this.tradeMsg = tradeMsg;
        }

        public int getTradeStatus() {
            return tradeStatus;
        }

        public void setTradeStatus(int tradeStatus) {
            this.tradeStatus = tradeStatus;
        }

        public long getTradeTime() {
            return tradeTime;
        }

        public void setTradeTime(long tradeTime) {
            this.tradeTime = tradeTime;
        }

        public double getVolume() {
            return volume;
        }

        public void setVolume(double volume) {
            this.volume = volume;
        }
    }
}
