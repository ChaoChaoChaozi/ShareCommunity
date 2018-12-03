basePath = 'http://www.share.com';
img_base_url= 'http://image.share.com';
post_cover_thumbnail='';
album_thumbnail='';

function escape(v) {
    var  entry = { "'": "&apos;", '"': '&quot;', '<': '&lt;', '>': '&gt;' };
    v = v.replace(/(['")-><&\\\/\.])/g, function ($0) { return entry[$0] || $0; });
    return v;
}