from typing import Self

from hexdoc_hexcasting.book.page.abstract_pages import PageWithOpPattern # pyright: ignore[reportMissingTypeStubs]
from hexdoc_hexcasting.metadata import HexContext
from hexdoc_hexcasting.utils.pattern import PatternInfo
from pydantic import ValidationInfo, model_validator

# Look mom, I'm here. Very top of Arasaka tower.
class LookupPWShapePage(PageWithOpPattern, type="hexcasting:lapisworks/pwshape"):
    idx_in_flags: int | None = None

    @property
    def patterns(self) -> list[PatternInfo]:
        return self._patterns
    
    @model_validator(mode="after")
    def _post_root_lookup(self, info: ValidationInfo):
        hex_ctx = HexContext.of(info)
        self._patterns = [hex_ctx.patterns[self.op_id]]
        return self
    
    @model_validator(mode="after")
    def _check_anchor(self) -> Self:
        if str(self.op_id) != self.anchor:
            raise ValueError(f"op_id={self.op_id} does not equal anchor={self.anchor}")
        # at least TRY to get it to work, this is temp to select the first
        # perworldshape pat for this next commit push
        self.op_id += "0"
        self.anchor += "0" # type: ignore
        return self
