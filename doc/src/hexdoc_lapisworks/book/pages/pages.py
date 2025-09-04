from typing import Self

from hexdoc_hexcasting.book.page.abstract_pages import PageWithOpPattern # pyright: ignore[reportMissingTypeStubs]
from hexdoc_hexcasting.metadata import HexContext
from hexdoc_hexcasting.utils.pattern import PatternInfo, Direction
from hexdoc.core.resource import ResourceLocation
from pydantic import ValidationInfo, model_validator

# Look mom, I'm here. Very top of Arasaka tower.
class LookupPWShapePage(PageWithOpPattern, type="hexcasting:lapisworks/pwshape"):
    idx_in_flags: int | None = None

    @property
    def patterns(self) -> list[PatternInfo]:
        return self._patterns
    
    @model_validator(mode="after")
    def _post_root_lookup(self, info: ValidationInfo):
        #hex_ctx = HexContext.of(info)
        self._patterns = [PatternInfo(id=ResourceLocation("lapisworks", "tmp"), startdir=Direction.NORTH_WEST, signature="aqaeawdwwwdwqwdwwwdweqaaaqaqwdddedeewqdedqewawawaw", is_per_world=True)]
        return self
    
    @model_validator(mode="after")
    def _check_anchor(self) -> Self:
        if str(self.op_id) != self.anchor:
            raise ValueError(f"op_id={self.op_id} does not equal anchor={self.anchor}")
        return self
