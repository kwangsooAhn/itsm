package co.brainz.framework.util

import java.util.UUID

public class UUIDGenerator {
	val uuid: String
		get() {
			return UUID.randomUUID().toString().replace("-", "")
		}
}