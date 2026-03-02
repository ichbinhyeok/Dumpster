@import gg.jte.Content
@param String title
@param String subtitle
@param boolean open
@param String classes
@param Content content
<details class="ui-accordion ${classes}" open="${open}">
    <summary>
        <span>${title}</span>
        @if(subtitle != null && !subtitle.isBlank())
            <small>${subtitle}</small>
        @endif
    </summary>
    <div class="ui-accordion__body">
        @content
    </div>
</details>
