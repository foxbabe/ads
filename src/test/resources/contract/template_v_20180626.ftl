<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="Content-Style-Type" content="text/css"/>
    <title></title>
    <style type="text/css">
        body {
            font-family: pingfang sc light;
            border:1px solid #000;
        }
        .center{
            text-align: center;
            width: 100%;
        }
        .right{
            text-align: right;
        }
        .base{
            line-height: 10px;
            text-indent: 20px;
        }
        .form_style{
            text-decoration:underline
        }

        .tb{
            width: 100%;
            alignment:center ;
            padding: 0px;
            border: 0;
            border-spacing: 0;
        }

        .thc {
            border: 0;
            text-align: left;
            margin: 0px ;
            padding: 0px;
        }
        .td {
            border:1px solid #000;
            text-align: left;
            margin: 0px ;
            padding: 0px;
        }
        .para_head{
            margin-top: 12px;
            margin-bottom: 0px;
            font-weight: bold;
            text-align: left;
            text-indent: 0;
        }

        p{
            margin-top: 12px;
            margin-bottom: 0px;
            text-indent: 2em;
            line-height: 26px;
        }
        .pitem{
            margin-top: 5px;
            margin-bottom: 0px;
            padding-left: 2em;
            line-height: 22px;
        }
        .pageStyle{
            padding-top: 30px;
        }
    </style>
</head>
<body>
<div class="pageStyle" style="margin-top: auto">
    <h1 class="center">众店宝电子媒体广告投放协议</h1>
    <h2 class="right">合同编号(${contractCode})</h2>
    <div >
        <p class="base" style="font-weight: bold">甲&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;方（广告主）：${firstPartyName}</p>
        <p class="base">责任联系人：${firstPartyResponsibilityPerson}</p>
        <p class="base">电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话：${firstPartyPhone}</p>
        <p class="base">指定送达地址：${firstPartyContractReceiveAddress}</p>
        <p class="base">E-Mail：${firstPartyEmail}</p>
        <br/>
        <p class="base" style="font-weight: bold">乙&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;方：${secondPartyName} </p>
        <p class="base">责任联系人：${secondPartyResponsibilityPerson}</p>
        <p class="base">电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话：${secondPartyPhone}</p>
        <p class="base">指定送达地址：${secondPartyContractReceiveAddress}</p>
        <p class="base">E-Mail：${secondPartyEmail}</p>
    </div>
    <div class="default_div">
        <h3>鉴于：</h3>
        <p >
            甲、乙双方本着平等合作、互惠互利的原则，经友好协商，根据《中华人民共和国合同法》、《中华人民共和国广告法》等有关法律法规的规定，就甲方在乙方运营的众店宝网点的众店宝设备电子屏（简称众店宝电子媒体）上投放甲方广告事宜达成一致，签订如下协议，以资双方共同遵守。</p>
        <h4 class="para_head">第1条广告位置、时长</h4>
        <div>
            <p >1.1双方同意甲方在以下乙方众店宝网点的众店宝电子媒体上发布广告：共 <span class="form_style"> ${amountOfCities} </span> 个城市，分别为<span class="form_style"> ${cities}  </span>   ；计  <span class="form_style"> ${storeOfAll}</span> 个网点；具体为：
                <span class="form_style">${topCity}</span>市  <span class="form_style">${storeOfTopCity}</span>  个网点；（网点清单详见附件一）。如网点有增加或变更的，双方可另行签署补充协议。</p>
            <p >1.2在不影响乙方在每个点位最高投放数量的基础上，甲方可根据自身需要选择广告投放的时长；广告画面的大小由乙方统一规定，具体要求详见附件二。</p>
        </div>
        <h4 class="para_head">第2条广告内容范围</h4>
        <div >
            <p >2.1甲方在乙方运营的众店宝电子媒体上投放的广告内容不得违背公序良俗，不得违背国家有关法律法规，需符合《中华人民共和国广告法》的各项具体规定。</p>
            <p >2.2甲方投放的广告内容需在甲方营业执照规定的经营范围内,且需符合本协议约定。</p>
        </div>
        <h4 class="para_head">第3条价款、付款方式</h4>
        <div>
            <p>3.1本次广告投放的媒体费用总计：<span class="form_style"> ${mediumCost} </span>  元。</p>
            <p>3.2本次广告投放的制作费用总计：<span class="form_style"> ${productCost}</span>   元。若在合同期内需要更换广告画面，则由双方另外签署补充协议。</p>
            <p>3.3根据上述第3.1条和第3.2条的费用约定，本次广告投放的费用总价款为：<span class="form_style"> ${totalCost}  </span> 元（大写：<span class="form_style"> ${totalCostUpper} </span>  元）。甲方在本协议签署后 <span class="form_style"> ${signAfterDay} </span> 日内应按本协议支付前述费用；甲方逾期支付前述费用的，每逾期一日需按应付未付金额的千分之三向乙方支付逾期付款违约金。</p>
            <p>3.4乙方收款账户如下：</p>
            <div>
                <p class="pitem">乙方户名：${bankAccountName} </p>
                <p class="pitem">乙方账号：${bankAccountNumber}</p>
                <p class="pitem">乙方开户行：${bankName}</p>
            </div>
            <p>3.5乙方应开具符合规定的正式发票，并注明对应期间。</p>
        </div>
        <h4 class="para_head">第4条权利与义务</h4>
        <div>
            <p>4.1 甲方的权利与义务</p>
            <p>4.1.1甲方有权在本协议约定的乙方运营的众店宝电子媒体网点投放自己的广告（即甲方作为广告主的广告），甲方不得在本协议约定的网点投放、发布或代理其他公司或个人的广告；</p>
            <p>4.1.2甲方发布的广告需为甲方公司自营业务的广告，广告内容需在甲方营业执照规定的经营业务范围内，且符合本协议约定，对于发布内容、发布时间、发布网点在发布前需以电子档的形式通过电子邮件通知乙方并经乙方审核确认；</p>
            <p>4.1.3不侵权义务：</p>
            <p>甲方广告的创意、文案、软文撰写、设计、策划由甲方自行负责，并不得侵犯任何第三方的知识产权或其他权利（甲方的不侵权义务）。甲方应在实施发布前向乙方提供最终设计稿供乙方审核确认，乙方的审核不能免除或减轻甲方违反不侵权应当对第三方承担的责任，乙方的审核也不得被理解为乙方应当承担或部分承担甲方违反不侵权义务所产生的任何责任和后果。</p>
            <p> 4.1.4甲方应当按本协议约定付款；</p>
            <p>4.1.5甲方开展的广告业务不得损害乙方形象、不得与众店宝业务有竞争关系。</p>
            <p>4.2 乙方的权利与义务</p>
            <p>4.2.1本协议有效期内，本协议附件一中约定的网点以满足甲方投放广告需要为主。根据电子媒体的特殊性质，同一点位可投放多个广告，因此在不超过最高投放数量的基础上，乙方也有权在附件一约定的网点上发布其他广告；</p>
            <p>4.2.2乙方拥有众店宝电子媒体的广告运营权；</p>
            <p> 4.2.3乙方负责与各众店宝网点进行广告投放的商务洽谈事宜；</p>
            <p>4.2.4乙方有权对甲方投放的广告内容、质量、发布时间、发布网点等进行审核，对不符合本协议约定或可能遭致投诉或处罚的广告，乙方有权禁止发布或者撤销发布且无需承担责任，但需及时通知甲方。</p>
        </div>
        <h4 class="para_head">第5条协议期限及补充说明</h4>
        <div>
            <p>5.1本协议合作周期为  <span class="form_style"> ${totalMonths}</span>，合作期限自 <span class="form_style"> ${yearOfStart} </span>  年 <span class="form_style"> ${monthOfStart}</span> 月 <span class="form_style">${dayOfStart} </span> 日至 <span class="form_style"> ${yearOfEnd} </span>  年<span class="form_style"> ${monthOfEnd} </span> 月 <span class="form_style">${dayOfEnd} </span> 日止；实际投放周期为 <span class="form_style">  ${contractAdvertisementPeriod} </span>，广告投放日期以实际上刊日期为准。合作周期内的众店宝单个网点月度广告费用不变；后续如有广告网点增加，则甲乙双方补签协议。</p>
            <p>5.2合作期间，如遇乙方已投放的单个众店宝网点设备撤离，应提前通知甲方，该网点的广告协议自动终止，乙方应在事先得到甲方的书面确认后，选择其他网点设备进行替换；甲方也可要求退还该撤离网点内剩余的媒体费用。</p>
        </div>
        <h4 class="para_head">第6条保密条款</h4>
        <div>
            <p>6.1甲乙双方互负保密义务，未经对方书面许可，不得向第三方泄露履行本合同而知晓对方的商业秘密、工作计划、业务资料和交易情况等信息；</p>
            <p>6.2商业秘密包括但不限于乙方众店宝网点信息、乙方客户信息，甲方有关创意、传播策略，及双方营业状况、客户名单及其它技术信息和经营信息等；</p>
            <p>6.3一方违反保密责任的约定使对方受到损失的，违反保密责任的一方应承担违约赔偿责任。赔偿责任范围为守约方因泄密而遭受的直接损失及违约方因泄密行为而所得到的利益。</p>
            <p>6.4本条保密义务不因双方合作期限届满、合同终止或解除而免除、消除、或终止。</p>
        </div>
        <h4 class="para_head">第7条合同的变更、中止与解除</h4>
        <div>
            <p>7.1若受国家政策影响致使合同无法履行，甲、乙双方可协商解除合同。除前述约定外，在一方不存在违约的情况下，另一方欲变更、解除本合同，应提前1个月书面通知对方，并阐明理由，经对方书面同意后，方可对本合同及其附件进行变更、解除。</p>
            <p>7.2若遇不可抗力之情形，具体包括地震、自然灾害、战争、传染性疾病的蔓延、火灾、政府限电等不能预见、不能避免并不能克服的客观情况，使得合同无法继续履行，合同任一方可立即书面通知另一方，本合同即中止。因不可抗力导致无法继续履行合同义务的，不属于违约行为。不可抗力情形消失后，本合同自动恢复履行。</p>
            <p>7.3甲方有义务对广告的内容和表现形式进行审查，如该广告内容和表现形式发布后引起投诉纠纷的，或引发政府处罚或诉讼的，甲方除应承担全部责任和赔偿外，乙方还有权解除本协议并不退还费用；如给乙方造成损失的，乙方有权向甲方追偿。</p>
            <p>7.4一方如有以下违反本协议约定而构成违约的，另一方有权解除本协议，并可同时要求违约方承担本协议总价30%的违约责任：</p>
            <p>7.4.1未履行本协议约定的保密义务；</p>
            <p>7.4.2未经乙方书面同意，甲方转包或分包本协议项下的权利义务的；</p>
            <p>7.4.3其他严重违反本协议约定或有其他法律规定，致使合同目的不能实现的。</p>
        </div>
        <h4 class="para_head">第8条第三方投诉机制</h4>
        <p>如第三方对甲方发布或投放的广告投诉侵权并提供了基本证据的，乙方有权暂停或要求甲方暂停涉嫌侵权广告的播放。甲方能够提供相反证据证明广告不侵权的，乙方应立即恢复广告播放。乙方为处理第三方投诉暂停或要求甲方暂停广告播放的，不视为乙方违约。</p>
        <h4 class="para_head">第9条其他：</h4>
        <div>
            <p>9.1争议解决。甲乙双方涉及本合同履行而发生争议，应本着友好协商的方式解决。协商未果，双方均同意提交本协议签订地法院进行裁判；在争议解决期间，协议中未涉及争议部分的条款仍须履行。</p>
            <p>9.2协议份数。本协议自甲乙双方签章后生效；本协议一式贰份，甲、乙双方各执壹份，具有同等法律效力。本协议未尽事宜，双方可另行签署补充协议。</p>
            <p>9.3【补充约定】</p>
            <div style="padding-left: 24px;padding-top: 0px;">
                <p>${supplementary}</p>
            </div>
        </div>

        <p>在签署本协议时，各当事人对本协议的所有条款已经阅悉，均无异议，并对当事人之间的法律关系、有关权利义务和责任条款的法律含义有准确无误的理解。</p><p>
        <table border="0" style="border-spacing:0px 10px;">
            <tr border="0" >
                <td width="50%">甲方（签章）：</td>
                <td width="50%">乙方（签章）：</td>
            </tr>
            <tr border="0" >
                <td width="50%">授权人（签字）：</td>
                <td width="50%">授权人（签字）：</td>
            </tr>
            <tr border="0" >
                <td width="50%">签署日期：        年      月      日</td>
                <td width="50%">签署日期：        年      月      日</td>
            </tr>
        </table>
        </p>
    </div>

</div>
<div class="pageStyle">
<#if storeList?? && (storeList?size>0)>
    <h2 class="left">附件一、投放网点</h2>
    <table class="tb" style="display:  <#if (storeList?size > 0)>true<#else>none</#if>">
        <tr class="thc">
            <th class="td" width="8%">
                序号
            </th>
            <th class="td" width="30%">
                众店宝网点
            </th >
            <th class="td" width="55%">
                具体地址
            </th>
            <th class="td" width="12%">
                备注
            </th>
        </tr>
        <#list storeList as store>
            <tr class="thc">
                <td class="td" width="8%">${store_index+1}</td>
                <td class="td" width="30%">${store.storeName}</td>
                <td class="td" width="50%">${store.storeAddress}</td>
                <td class="td" width="12%">${store.remark}</td>
            </tr>
        </#list>
    </table>
</#if>
</div>
<div class="pageStyle">
<#if storeList?? && (storeList?size>0)>
    <h2 class="left">二、众店宝电子媒体广告画面尺寸、播放时长、轮播次数</h2>
    <table class="tb">
        <tr class="thc">
            <th class="td">
                画面尺寸
            </th>
            <th class="td">
                播放时长
            </th>
            <th class="td">
                轮播次数
            </th>
        </tr>
        <#list playinfos as playInfo>
            <tr class="thc">
                <td class="td">${playInfo.playsize}</td>
                <td class="td">${playInfo.playDuration}</td>
                <td class="td">${playInfo.playTime}</td>
            </tr>
        </#list>
    </table>
</#if>
</div>
</body>
</html>