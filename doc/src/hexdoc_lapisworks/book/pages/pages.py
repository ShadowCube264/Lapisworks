# pyright: reportUnknownMemberType=information

from typing import Self

from hexdoc_hexcasting.book.page.abstract_pages import PageWithOpPattern # pyright: ignore[reportMissingTypeStubs]
from hexdoc_hexcasting.metadata import HexContext
from hexdoc_hexcasting.utils.pattern import PatternInfo, Direction
from hexdoc.core.resource import ResourceLocation
from pydantic import ValidationInfo, model_validator
from .. import merge_pattern

# Look mom, I'm here. Very top of Arasaka tower.
class LookupPWShapePage(PageWithOpPattern, type="hexcasting:lapisworks/pwshape"):
    idx_in_flags: int | None = None # temp until i refactor ThemConfigFlags
    origins: list[dict[str, int]] | None = None

    @property
    def patterns(self) -> list[PatternInfo]:
        return self._patterns
    
    @model_validator(mode="after")
    def _post_root_lookup(self, info: ValidationInfo):
        if self.origins == None: raise TypeError("No origins provided for " + str(self.op_id))
        hex_ctx = HexContext.of(info)
        patterns: list[tuple[int, PatternInfo]] = []
        origins: dict[int, merge_pattern.HexCoord] = {}
        i = 1
        while True:
            pat = hex_ctx.patterns.get(self.op_id + str(i))
            if pat == None:
                break
            patterns.append((i, pat))
            i += 1
            
            coord: dict[str, int] = self.origins[i - 1]
            q: int = coord["q"]
            r: int = coord["r"]
            if q == 0 and r == 0: continue
            origins[i] = merge_pattern.HexCoord(q=q, r=r)
        self._patterns = [merge_pattern.overlay_patterns(self.op_id, patterns, origins)]
        return self
    
    @model_validator(mode="after")
    def _check_anchor(self) -> Self:
        if str(self.op_id) != self.anchor:
            raise ValueError(f"op_id={self.op_id} does not equal anchor={self.anchor}")
        return self
